package com.example.malls.domain.service;

import com.example.malls.domain.dto.*;
import com.example.malls.domain.repository.BoardRepository;
import com.example.malls.domain.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
class BoardServiceImplTest {
    @Autowired
    private BoardService service;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @Transactional
    public void testRemove(){
        Long bno = 10L;
        replyRepository.deleteByBoard_Bno(bno);
        service.remove(bno);}

    @DisplayName("Querydsl 튜플 처리 - 게시판 / 이미지 /  댓글 수")
    @Transactional
    @Test
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, pageable);

        log.info("-".repeat(30));
        log.info(result.getTotalElements());

        result.getContent().stream().forEach(boardListAllDTO -> log.info(boardListAllDTO));

    }


    /* 첨부파일 */
    @DisplayName("이미지와 같이 저장")
    @Test
    public void testRegisterWithImages(){
        log.info(service.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File Register Test")
                .content("Sample Content")
                .price(10400L)
                .writer("User11")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID().toString()+"_aaa.jpg",
                        UUID.randomUUID().toString()+"_bbb.jpg",
                        UUID.randomUUID().toString()+"_ccc.jpg",
                        UUID.randomUUID().toString()+"_ccc.jpg"
                )
        );

        Long bno = service.register(boardDTO);

        log.info("BNO : "+bno);
    }

    @DisplayName("게시물과 이미지 조회")
    @Transactional
    @Test
    public void testReadAll(){
        Long bno = 402L;

        BoardDTO dto = service.get(bno);

        log.info(dto);

        for(String fileName : dto.getFileNames()){
            log.info(fileName);
        }
    }


    @DisplayName("첨부파일 수정")
    @Transactional
    @Test
    @Commit
    public void testModify(){
        BoardDTO boardDTO = service.get(402L);
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(402L)
//                .title("Update Sample")
//                .content("Update Content")
//                .price(33550L)
//                .build();

        // 첨부 파일 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

        service.modify(boardDTO);
    }

    @DisplayName("게시물 목록 처리")
    @Test
    public void testListWithAll(){
        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(20)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = service.listWithAll(requestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+" : "+boardListAllDTO.getBoardImages());

            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages()){
                    log.info(boardImage);
                }
            }

            log.info("-".repeat(30));
        });
    }
}


