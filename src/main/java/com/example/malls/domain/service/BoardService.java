package com.example.malls.domain.service;

import com.example.malls.domain.dto.*;
import com.example.malls.domain.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO get(Long bno);

    PageResponseDTO<BoardDTO> getList(PageRequestDTO requestDTO);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);


    // 댓글 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO requestDTO);

    // 게시글의 이미지, 댓글 수 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO requestDTO);

    /* Convert */
    default Board dtoToEntity(BoardDTO dto){
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .price(dto.getPrice())
                .writer(dto.getWriter())
                .build();


        // 이미지 등록
        if(dto.getFileNames() != null){
            dto.getFileNames().forEach(fileName ->{
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    default BoardDTO entityToDto(Board board){
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .price(board.getPrice())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();


        // 이미지 조회
        List<String> fileNames = board.getImageSet().stream().sorted()
                .map(boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName())
                .collect(Collectors.toList());

        dto.setFileNames(fileNames);

        return dto;

    }
}
