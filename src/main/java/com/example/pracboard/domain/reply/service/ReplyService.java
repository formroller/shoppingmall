package com.example.pracboard.domain.reply.service;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.dto.ReplyDTO;
import com.example.pracboard.domain.reply.entity.Reply;

import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);
    List<ReplyDTO> getList(Long bno);


    default Reply dtoToReply(ReplyDTO replyDTO){

        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .replyer(replyDTO.getReplyer())
                .replyText(replyDTO.getReplyText())
                .board(board)
                .build();

        return reply;
    }

    default ReplyDTO replyToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .replyer(reply.getReplyer())
                .replyText(reply.getReplyText())
                .regDate(reply.getRegDate())
                .build();

        return dto;
    }
}
