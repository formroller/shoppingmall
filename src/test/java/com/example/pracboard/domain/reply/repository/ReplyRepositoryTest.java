package com.example.pracboard.domain.reply.repository;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
                    .writer("guest"+i)
                    .build();

            repository.save(reply);
        });
    }

}