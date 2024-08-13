package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Board;
import com.example.malls.domain.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch{
    // EntityGraph 조회(하위 엔티티 지연 로딩)
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(Long bno);




}
