package com.example.pracboard.domain.reply.repository;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.entity.Reply;
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

@SpringBootTest
@Log4j2
class ReplyRepositoryTest {
    @Autowired
    private ReplyRepository repository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,300).forEach(i->{
            long bno = (long)(Math.random() * 100)+1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply Text -- "+i)
                    .board(board)
                    .replyer("guest"+i)
                    .build();

            repository.save(reply);
        });
    }
    
    @DisplayName("특정 게시물의 댓글 조회 및 인덱스")
    @Test
    public void testBoardReplies(){
        Long bno = 100L;
        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());
        Page<Reply> result = repository.listOfBoard(bno, pageable);
        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
    

}