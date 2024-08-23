package com.example.malls.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberJoinDTO {
    private String mid;
    private String mpw;
    private String email;
//    private String nickname;
    private boolean del;
    private boolean social;
}
