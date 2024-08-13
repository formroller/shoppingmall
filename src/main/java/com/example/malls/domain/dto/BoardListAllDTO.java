package com.example.malls.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {
    /* Board, Image, 댓글 갯수 모두 처리 용도*/

    private Long bno;
    private String title;
    private String writer;
    private long price;
    private LocalDateTime regDate;
    private Long replyCnt;
    private List<BoardImageDTO> boardImages;
}
