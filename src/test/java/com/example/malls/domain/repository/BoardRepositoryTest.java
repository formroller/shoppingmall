package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Board;
import com.example.malls.domain.entity.BoardImage;
import com.example.malls.domain.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardRepositoryTest {
    @Autowired
    private BoardRepository repository;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,400).forEach(i->{
            Board board = Board.builder()
                    .title("title --- "+i)
                    .content("Content is ==="+i)
                    .price(i*((int)(Math.random()*100)+10*3024))
                    .writer("writer"+i)
                    .build();

            for(int j=0; j < 3; j++){
                if(i % 5 ==0 ){
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
            }
            repository.save(board);
        });
    }


    @Transactional
    @Commit
    @Test
    public  void testInsertImage(){
        IntStream.rangeClosed(1,400).forEach(i->{
            Long bno = (long)i;

            repository.findById(bno).ifPresent(board -> {
                try{
                    if(board.getBno() % 5 == 0){
                        for(int j=0; j < 3; j++){
                            board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
                        }
                        repository.save(board);
                    }
                }catch (Exception e){
                    log.error("Exception Error :",e);
                }
            });
        });
//        IntStream.rangeClosed(1,400).forEach(i->{
//            Long bno = (long)i;
//            Optional<Board> optionalBoard = repository.findById(bno);
//
//            try{
//                if(optionalBoard.isPresent() && optionalBoard.get().getBno() % 5!=0){
//                    Board board = optionalBoard.get();
//
//                    for(int j=0; j<3; j++){
//                        board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
//                        repository.save(board);
//                    }
//                }
//            }catch (NoSuchElementException e){
//                log.error("NullPointException :",e);
//            }
//        });
    }
    @Test
    @Transactional
    public void testRemove(){

        IntStream.rangeClosed(1,1500).forEach(i->{
            Long boardId = (long)i;
//            replyRepository.deleteById(boardId);

            repository.deleteById(boardId);
        });
    }
    @DisplayName("Board - BoardImage insert test")
    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .price(23900)
                .writer("writer2")
                .build();

        for(int i=0; i<3; i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        } // end for

        repository.save(board);
    }

    @DisplayName("@EntityGraph")
    @Test
    public void testReadWithImages(){
        Optional<Board> result = repository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("-".repeat(30));
//        log.info(board.getImageSet());
        for(BoardImage images : board.getImageSet()){
            log.info(images);
        }

    }

    @DisplayName("첨부파일 수정")
    @Test
    public void testModifyImages(){
        Optional<Board> result = repository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        // 기존 첨부 파일 삭제
        board.clearImage();

        // 새로운 첨부파일 등록
        for(int i=0; i < 2; i++){
            board.addImage(UUID.randomUUID().toString(), "updateFile"+i+".jpg");
        }

        repository.save(board);
    }

    @DisplayName("첨부파일 삭제")
    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1501L;

    }


    @DisplayName("Batch Size - 게시판, 이미지, 댓글 조회")
    @Transactional
    @Test
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        repository.searchWithAll(null, pageable);

    }

}