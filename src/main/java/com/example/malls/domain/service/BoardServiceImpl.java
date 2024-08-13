package com.example.malls.domain.service;

import com.example.malls.domain.dto.*;
import com.example.malls.domain.entity.Board;
import com.example.malls.domain.repository.BoardRepository;
import com.example.malls.domain.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = dtoToEntity(boardDTO);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public BoardDTO get(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow();

        BoardDTO dto = entityToDto(board);

        return dto;
    }

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO requestDTO) {

        String keyword = requestDTO.getKeyword();
        Pageable pageable = requestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board->entityToDto(board)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getPrice());

        // 첨부파일 처리
        board.clearImage();

        if(boardDTO.getFileNames() != null){
            for(String fileName : boardDTO.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void remove(Long bno) {
        replyRepository.deleteByBoard_Bno(bno);

        Board board = boardRepository.findById(bno).orElseThrow(()->new NoSuchElementException("Board Not Found"));

        board.clearImage();

        boardRepository.delete(board);

    }


    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO requestDTO) {

        String keyword = requestDTO.getKeyword();
        Pageable pageable = requestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();

    }

    // 게시글의 이미지 / 댓글수 처리
    @Transactional
    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO requestDTO) {

        String keyword = requestDTO.getKeyword();
        Pageable pageable = requestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
