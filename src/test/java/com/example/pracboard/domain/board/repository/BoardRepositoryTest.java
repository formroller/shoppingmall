package com.example.pracboard.domain.board.repository;

import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.member.entity.Member;
import com.example.pracboard.domain.reply.entity.Reply;
import com.example.pracboard.domain.reply.repository.ReplyRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository repository;

    @Autowired
    private ReplyRepository replyRepository;

    @DisplayName("추가 테스트")
    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i->{
//             Board board =  Board.builder()
//                    .title("Sample Product"+i)
//                    .price(i*1402)
//                    .content("Content is ---- "+i)
//                    .build();

            Member member = Member.builder().email("user"+i+"@aa.com").build();

            Board board =  Board.builder()
                    .title("Sample Product"+i)
                    .price(i*1402)
                    .content("Content is ---- "+i)
                    .writer(member)
                    .build();

            repository.save(board);

            System.out.println(board);
        });
    }

    @Transactional
    @Test
    public void testGet(){
        Optional< Board> result = repository.findById(97L);

         Board board = result.get();

        System.out.println(board);
        System.out.println();
        System.out.println(board.getWriter());
    }

    @Test
    public void testModify(){
        Long bno = 298L;

        Optional< Board> result = repository.findById(bno);

         Board board = result.orElseThrow();

        board.change("Update Title", 1234132L, "update content1234");

        repository.save(board);

    }

    @DisplayName("QueryDsl - Search Test")
    @Test
    public void testSearch(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());

        repository.search(pageable);
    }


    @DisplayName("Keyword Search")
    @Test
    public void testSearchKeyword(){
        PageRequestDTO requestDTO = new PageRequestDTO().builder().keyword("test").build();

//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//
//        Page<Board> result = repository.findAllKeywordWithConditions(requestDTO, pageable);
//
//        result.getContent().forEach(board -> log.info(board));
    }

    @DisplayName("Board With Writer")
    @Test
    public void testReadWithWriter(){
        Object result = repository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    @DisplayName("Board With Reply")
    @Test
    public void testReadWithReply(){
        List<Object[]> result = repository.getBoardWithReply(97L);

        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    void getBoardWithReplyCount() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = repository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;

            System.out.println(Arrays.toString(arr));
        });
    }


    @Test
    public void testGetBoardByBno(){
        Object result = repository.getBoardByBno(100L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }
}
