package com.example.pracboard.domain.board.repository;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.entity.Reply;
import com.example.pracboard.domain.reply.repository.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository repository;

    @Autowired
    private ReplyRepository replyRepository;

    @DisplayName("추가 테스트")
    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Board board = Board.builder()
                    .title("Sample Product"+i)
                    .price(i*1402)
                    .build();

            repository.save(board);

            System.out.println(board);
        });
    }

    @Test
    public void testGet(){
        Optional<Board> result = repository.findById(100L);

        Board board = result.get();

        Optional<Reply> replies = replyRepository.findById(board.getBno());

        System.out.println(board);
        System.out.println();
        System.out.println(replies);
    }
}
