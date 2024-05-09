package com.example.pracboard.domain.reply.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReplyDTO {
    private Long rno;
    private Long bno;
    private String replyText;
    private String replyer;
    private LocalDateTime regDate;

}
