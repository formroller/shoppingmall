package com.example.malls.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String writer;
    private long price;
    private LocalDateTime regDate;

    private Long replyCnt;
}
