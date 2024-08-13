package com.example.malls.domain.dto;

import com.example.malls.domain.dto.upload.UploadResultDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private long price;

    private String writer;
    private int replyCnt;

    private LocalDateTime regDate, modDate;

    // 첨부파일 명
    private List<String> fileNames;
//    private List<UploadResultDTO> fileNames;
}
