package com.example.pracboard.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardMemberReplyCntDTO {

    private Long bno;
    private String title;
    private String content;
    private String writerEmail; // 작성자 메일
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCnt; // 댓글 수
}
