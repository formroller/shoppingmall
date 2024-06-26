package com.example.pracboard.domain.board.service;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.member.entity.Member;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    /*CRUD*/
    Long register(BoardDTO boardDTO);

//    PageResponseDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO);

    BoardListReplyCountDTO get(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO, Object[]> getSearch(PageRequestDTO requestDTO);



    /*Convert*/
    default Board toEntity(BoardDTO dto){
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .content(dto.getContent())
                .build();

        return board;
    }

    default BoardDTO toDTO(Board board, Long replyCnt){
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .price(board.getPrice())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .replyCnt(replyCnt.intValue())
                .build();

        return dto;
    }
    default BoardListReplyCountDTO entityToCountDTO(Board board, Member member, Long replyCnt){
        BoardListReplyCountDTO dto = BoardListReplyCountDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .price(board.getPrice())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCnt(replyCnt.intValue())
                .build();

        return dto;
    }
}
