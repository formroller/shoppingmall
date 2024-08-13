package com.example.malls.domain.service;

import com.example.malls.domain.dto.PageRequestDTO;
import com.example.malls.domain.dto.PageResponseDTO;
import com.example.malls.domain.dto.ReplyDTO;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.entity.Reply;
import org.springframework.data.domain.PageRequest;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);


    // 댓글 목록 페이징
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO requestDTO);
    /* Convert */

    default Reply dtoToReply(ReplyDTO dto, Board board){

        Reply reply = Reply.builder()
//                .board(board)
                .board(board)
                .rno(dto.getRno())
                .replyer(dto.getReplyer())
                .replyText(dto.getReplyText())
                .build();
        return reply;
    }

    default ReplyDTO replyToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .bno(reply.getBoard().getBno())
                .replyer(reply.getReplyer())
                .replyText(reply.getReplyText())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();

        return dto;
    }

}
