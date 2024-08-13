package com.example.malls.domain.service;

import com.example.malls.domain.dto.PageRequestDTO;
import com.example.malls.domain.dto.PageResponseDTO;
import com.example.malls.domain.dto.ReplyDTO;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.entity.Reply;
import com.example.malls.domain.repository.BoardRepository;
import com.example.malls.domain.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {

        log.info(replyDTO);

        Board board = boardRepository.findById(replyDTO.getBno()).orElseThrow(
                () -> new DataIntegrityViolationException("Invalid board ID"));

        Reply reply = dtoToReply(replyDTO, board);

        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        return replyToDTO(reply);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> reply = replyRepository.findById(replyDTO.getRno());

        Reply result = reply.orElseThrow();

        result.changeText(replyDTO.getReplyText());

        replyRepository.save(result);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.findById(rno).orElseThrow();
        replyRepository.deleteById(rno);
    }


    // 댓글 목록 페이징
    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() <= 0? 0 : requestDTO.getPage()-1,
                requestDTO.getSize(),
                Sort.by("rno").descending());


        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDTO> dtoList = result.getContent().stream()
                .map(reply -> replyToDTO(reply)).collect(Collectors.toList());


        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }


}
