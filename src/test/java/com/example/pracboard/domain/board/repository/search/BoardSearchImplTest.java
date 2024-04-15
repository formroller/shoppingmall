package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.repository.BoardRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardSearchImplTest {
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void beforeEach(){
        PageRequestDTO requestDTO = PageRequestDTO.builder().keyword("test").build();
    }

    @Test
    public void testSearchKeyword(){

//        String keyword = requestDTO.;
        PageRequestDTO requestDTO = PageRequestDTO.builder().keyword("test").build();
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getSearchList(requestDTO, pageable);
    }

}