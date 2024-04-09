package com.example.pracboard.domain.reply.repository;

import com.example.pracboard.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
