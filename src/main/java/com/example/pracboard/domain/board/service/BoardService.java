package com.example.pracboard.domain.board.service;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import com.querydsl.core.BooleanBuilder;

public interface BoardService {

    /*CRUD*/
    Long register(BoardDTO boardDTO);

    PageResponseDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO);

    BoardDTO get(Long bno);

    void modify(BoardDTO boardDTO);



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

    default BoardDTO toDTO(Board board){
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .price(board.getPrice())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .build();

        return dto;
    }
}
