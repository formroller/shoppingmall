package com.example.pracboard.domain.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String replyer;
    private String title;
    private LocalDateTime regDate;
    private Long replyCnt;
}
