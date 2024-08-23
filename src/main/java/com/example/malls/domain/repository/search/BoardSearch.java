package com.example.malls.domain.repository.search;

import com.example.malls.domain.dto.BoardListAllDTO;
import com.example.malls.domain.dto.BoardListReplyCountDTO;
import com.example.malls.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> searchAll(String keyword, Pageable pageable);

//    Page<BoardListReplyCountDTO> searchWithAll(String keyword, Pageable pageable);
    Page<BoardListAllDTO> searchWithAll(String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String keyword,Pageable pageable);
}
