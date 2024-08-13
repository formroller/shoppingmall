package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Board;
import com.example.malls.domain.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("insert test")
    @Test
    public void testInsert(){
        Long bno=302L;

        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글 - 34d21")
                .replyer("replyer7")
                .build();

        replyRepository.save(reply);
    }

    @DisplayName("댓글 더미 추가")
    @Test
    public void testDummies(){
        IntStream.rangeClosed(1, 400).forEach(i -> {
            Long bno = (long) (Math.random() * 400) + 1; // 1부터 400까지의 값 생성
            try {
                Optional<Board> optionalBoard = boardRepository.findById(bno);
                if (optionalBoard.isPresent()) {
                    Board board = optionalBoard.get();
                    log.info("Found board: " + board);

                    IntStream.rangeClosed(1, 20).forEach(j -> {
                        try {
                            Reply reply = Reply.builder()
                                    .board(board)
                                    .replyText("Test -- " + bno * j)
                                    .replyer("replyer" + j)
                                    .build();

                            log.info("Reply is " + reply);
                            replyRepository.save(reply);
                            log.info("Reply saved successfully");
                        } catch (Exception e) {
                            log.error("Failed to save reply: ", e);
                        }
                    });
                } else {
                    log.info(bno + "에 해당하는 게시판을 찾을 수 없습니다.");
                }
            } catch (NoSuchElementException e) {
                log.error("NoSuchElementException occurred: ", e);
            } catch (Exception e) {
                log.error("Unexpected exception occurred: ", e);
            }
        });
    }

    // 개선 코드
//    public void testDummies() {
//        IntStream.rangeClosed(1, 400).forEach(this::addRepliesToRandomBoard);
//    }
//
//    private void addRepliesToRandomBoard(int index) {
//        Long boardId = (long) (Math.random() * 400) + 1; // 1부터 400까지의 값 생성
//        boardRepository.findById(boardId).ifPresentOrElse(
//                board -> IntStream.rangeClosed(1, 5).forEach(i -> saveReply(board, i, boardId)),
//                () -> log.info("Board with ID " + boardId + " not found")
//        );
//    }
//
//    private void saveReply(Board board, int index, Long boardId) {
//        try {
//            Reply reply = Reply.builder()
//                    .board(board)
//                    .replyText("Test -- " + boardId * index)
//                    .replyer("replyer" + index)
//                    .build();
//            replyRepository.save(reply);
//            log.info("Saved reply: " + reply);
//        } catch (Exception e) {
//            log.error("Failed to save reply for board ID " + boardId, e);
//        }
//    }
//}


    @DisplayName("listOfBoard")
    @Test
    public void testBoardReplies(){
        Long bno=302L;

        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> log.info(reply));
    }

    @DisplayName("게시물과 첨부파일 삭제")
    @Test
    public void testRemoveAll(){
        Long bno = 401L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }
}