package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.repository.BoardRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@SpringBootTest
@Log4j2
//@DataJpaTest // 별도 스프링 빈 등록않고 엔티티 및 EM만 등록해 테스트
class BoardSearchImplTest {
    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void testSearchKeyword(){

//        String keyword = requestDTO.;
        PageRequestDTO requestDTO = PageRequestDTO.builder().keyword("test").build();
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getSearchList(requestDTO, pageable);
    }

    @Test
    public void testGetKeyword(){


        PageRequestDTO requestDTO = PageRequestDTO.builder().keyword("test").build();
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getKeywords(requestDTO.getKeyword(), pageable);
        boardRepository.getKeywords(requestDTO.getKeyword(), pageable);

        result
                .stream()
                .map(t-> Arrays.stream(t).toArray())
                .forEach(System.out::println);

        System.out.println("-".repeat(40));

        log.info(result.getSize());

        result.stream().forEach(tuple -> {
            System.out.println(tuple);
        });
    }
}
