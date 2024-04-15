package com.example.pracboard.domain.board.service;

import com.example.pracboard.domain.board.dto.BoardDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.board.repository.BoardRepository;
import com.example.pracboard.global.page.PageRequestDTO;
import com.example.pracboard.global.page.PageResponseDTO;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;

    @Override
    public Long register(BoardDTO boardDTO) {
        log.info(boardDTO);

        Board board = toEntity(boardDTO);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResponseDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());

        Page<Board> result = repository.findAll(pageable);

        Function<Board, BoardDTO> fn = (en -> toDTO(en));

        return new PageResponseDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Optional<Board> result = repository.findById(bno);

        result.orElseThrow();

        return result.isPresent() ? toDTO(result.get()) : null;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
//        Optional<Board> result = repository.findById(boardDTO.getBno());
//        Board result = repository.findById(boardDTO.getBno());

//        Board board = result.orElseThrow();

//        if(board != null){
//            board.change(board.getTitle(), board.getPrice(), board.getContent());
//
//            repository.save(board);

        Board result = repository.getReferenceById(boardDTO.getBno());

        if(result != null){
            result.change(boardDTO.getTitle(),
                    boardDTO.getPrice(),
                    boardDTO.getContent());
        }


    }

    // 검색 처리
    @Override
    public PageResponseDTO<BoardDTO, Object[]> getSearch(PageRequestDTO requestDTO) {
//        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());

//        BooleanBuilder builder = repository.getSearchList(requestDTO);

//        Page<Board> result = repository.findAll(builder, pageable);
//
//        Function<Board, BoardDTO> fn = (board -> toDTO(board));
//
//        return new PageResponseDTO<>(result, fn);

//        return null;
        /*querydsl*/
//        Function<Object[], BoardDTO> fn = (en->
//                toDTO((Board)en[0], (Long)))

        Function<Object[], BoardDTO> fn = (en -> entityToCountDTO((Board) en[0], (Long) en[1]));

        Page<Object[]> result = repository.getSearchList(
                requestDTO,
//                requestDTO.getKeyword(),
                requestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResponseDTO<>(result, fn);
    }

}
