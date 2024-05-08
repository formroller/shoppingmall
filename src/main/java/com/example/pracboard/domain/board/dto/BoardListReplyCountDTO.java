package com.example.pracboard.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private long price;
    private String content;
    private String writerEmail;
    private String writerName;
    private LocalDateTime regDate;

    private int replyCnt;
}
