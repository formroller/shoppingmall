package com.example.domain;

import com.example.malls.domain.dto.BoardListReplyCountDTO;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

//@SpringBootTest(classes = MallsApplication.class)
@SpringBootTest
@Log4j2
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,300).forEach(i->{

            Board board = Board.builder()
                    .title("Title -- "+i)
                    .content(" === Content =>"+i)
                    .price(((int)(Math.random()*100)+1)*2042)
                    .build();

            boardRepository.save(board);
        });
    }


    @DisplayName("Board + Reply Count")
    @Test
    public void testBoardWithReplyCount(){
        String keyword = "sss";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(keyword, pageable);

        log.info(result.getTotalPages()); // total pages
        log.info(result.getSize()); // page size
        log.info(result.getNumber()); // page number
        log.info(result.hasPrevious() +" : "+ result.hasNext()); // prev next
        result.getContent().forEach(board -> log.info(board));
    }
}