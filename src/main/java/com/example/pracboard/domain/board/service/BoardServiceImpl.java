package com.example.pracboard.domain.board.service;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.repository.BoardRepository;
import com.example.pracboard.domain.member.entity.Member;
import com.example.pracboard.domain.reply.repository.ReplyRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardServiceImpl implements BoardService {
    private final ReplyRepository replyRepository;

    private final BoardRepository repository;

    @Override
    public Long register(BoardDTO boardDTO) {
        log.info(boardDTO);

        Board board = toEntity(boardDTO);

        repository.save(board);

        return board.getBno();
    }

//    @Override
//    public PageResponseDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO) {
//        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
//
//        Page<Board> result = repository.findAll(pageable);
//
//        Function<Board, BoardDTO> fn = (this::toDTO);
//
//        return new PageResponseDTO<>(result, fn);
//    }


    // 검색 처리
    @Override
    public PageResponseDTO<BoardDTO, Object[]> getSearch(PageRequestDTO requestDTO) {

        /*querydsl*/
//        Function<Object[], BoardDTO> fn = (en -> toDTO((Board) en[0], (Long) en[1]));
//
//        Page<Object[]> result = repository.getKeywords(
//                requestDTO.getKeyword(),
//                requestDTO.getPageable(Sort.by("bno").descending())
//        );
//
//        return new PageResponseDTO<>(result, fn);
        Function<Object[], BoardDTO> fn = (en -> toDTO((Board) en[0], (Long) en[1]));

        Page<Object[]> result = repository.getKeywords(
                requestDTO.getKeyword(),
                requestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResponseDTO<>(result, fn);
    }

//    @Override
//    public BoardDTO get(Long bno) {
//        Optional<Board> result = repository.findById(bno);
//
//        result.orElseThrow();
//
//        return result.map(this::toDTO).orElse(null);
//    }
    @Override
    public BoardListReplyCountDTO get(Long bno){
        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToCountDTO((Board) arr[0], (Member) arr[1],(Long) arr[2]);
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = repository.findById(boardDTO.getBno());

        if(result.isPresent()){
            Board entity = result.get();

            entity.change(boardDTO.getTitle(),
                    boardDTO.getPrice(),
                    boardDTO.getContent());

            repository.save(entity);
        }

//        return toDTO(result.get()).getBno();

    }

    @Override
    public void remove(Long bno){
        repository.deleteById(bno);
    }



//    @Override
//    public PageResponseDTO<BoardListReplyCountDTO, Object[]> getKeyword(PageRequestDTO requestDTO, Pageable pageable) {
//    public PageResponseDTO<BoardListReplyCountDTO, Board> getKeyword(PageRequestDTO requestDTO, Pageable pageable) {
////    public PageResponseDTO<Board, Object[]> getKeyword(PageRequestDTO requestDTO, Pageable pageable) {
//        Function<Object[], BoardDTO> fn = (en -> entityToCountDTO((Board) en[0], (Long) en[1]));
//
//
//        Page<Board> result = repository.findAllKeywordWithConditions(
//                requestDTO, requestDTO.getPageable(Sort.by("bno").descending())
//        );
//
//        return new PageResponseDTO<>(result, fn);
//    }

//    @Override
//    public PageResponseDTO<BoardListReplyCountDTO, Board> getKeyword(PageRequestDTO requestDTO, Pageable pageable) {
//        Function<Board, BoardListReplyCountDTO> fn = (en -> entityToCountDTO(en, (Long) en[1]));
//
//        Page<Board> result = repository.findAllKeywordWithConditions(
//                requestDTO, pageable
//        );
//
//        return new PageResponseDTO<>(result, fn);
//        Function<Board, BoardListReplyCountDTO> fn = (en -> entityToCountDTO((Board) en[0],(Long) en[1]));
//
//        // Page<Board>를 Page<EN>으로 변경
//        Page<Board> result = repository.findAllKeywordWithConditions(
//                requestDTO, pageable
//        );
//
//        return new PageResponseDTO<>(result, fn);
//    }

}


