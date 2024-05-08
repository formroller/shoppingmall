package com.example.pracboard.domain.reply.repository;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.domain.reply.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.board.bno=:bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno")
    void deleteByBno(@Param("bno")Long bno);


    List<Reply> getRepliesByBoardOrderByRno(Board board);

}
