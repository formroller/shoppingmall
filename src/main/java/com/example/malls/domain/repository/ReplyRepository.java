package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno=:bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    void deleteByBoard_Bno(Long bno);
}
