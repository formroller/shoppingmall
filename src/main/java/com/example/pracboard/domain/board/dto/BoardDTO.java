package com.example.pracboard.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardDTO {
    private Long bno;
    private String title;
    private long price;
    private String content;
    private LocalDateTime regDate;

    private LocalDateTime modDate;
    private int replyCnt;



    private int cartCnt; // 카트 담은 수
}
