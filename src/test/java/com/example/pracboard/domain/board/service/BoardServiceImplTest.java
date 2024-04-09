package com.example.pracboard.domain.board.service;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.repository.BoardRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private BoardService service;

    @DisplayName("getList")
    @Test
    public void testList(){
        PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<BoardDTO, Board> resultDTO = service.getList(requestDTO);

        for(BoardDTO boardDTO : resultDTO.getDtoList()){
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testGet(){
        Long bno = 11L;

        System.out.println(service.get(bno));

    }
}