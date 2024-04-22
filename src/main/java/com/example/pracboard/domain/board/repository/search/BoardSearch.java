package com.example.pracboard.domain.board.repository.search;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.global.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;

public interface BoardSearch {
    Page<Board> search(Pageable pageable);


    Page<Object[]> getSearchList(PageRequestDTO requestDTO, Pageable pageable);

    Page<Object[]> getKeywords(String keyword, Pageable pageable);

}
