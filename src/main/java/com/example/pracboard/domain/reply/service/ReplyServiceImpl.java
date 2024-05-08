package com.example.pracboard.domain.reply.service;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.dto.ReplyDTO;
import com.example.pracboard.domain.reply.entity.Reply;
import com.example.pracboard.domain.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository repository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToReply(replyDTO);

        repository.save(reply);

        return reply.getRno();
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Reply reply = dtoToReply(replyDTO);

        repository.save(reply);

    }

    @Override
    public void remove(Long rno) {
        repository.deleteById(rno);
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {

        List<Reply> result = repository.getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());

        return result.stream().map(this::replyToDTO).collect(Collectors.toList());

    }
}
