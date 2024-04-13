package com.example.pracboard.domain.reply.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReplyDTO {
    private Long rno;
    private String bno;
    private String text;
    private String replyer;
    private LocalDateTime regDate;

}
