package com.example.malls.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {
    /* BoardImage 처리 용도 */
    private String uuid;
    private String fileName;
    private int ord;

}
