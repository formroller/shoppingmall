package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.dto.BoardListReplyCountDTO;
import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.global.page.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search(Pageable pageable);

    Page<BoardListReplyCountDTO> searchKeyword(String keyword, Pageable pageable);

    Page<Object[]> getSearchList(PageRequestDTO requestDTO, Pageable pageable);

    Page<Object[]> getSearchKeyword(String keyword, Pageable pageable);
}
