package com.example.malls.domain.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    /*업로드 반환 결과 처리
    * 1. 업로드된 파일의 UUID 값, 파일명, 이미지 여부 객체로 구성
    * 2. getLink() 통해 첨부파일의 경로 처리에 사용
    */

    private String uuid;

    private String fileName;

    private boolean img;

    public String getLink(){
        if(img){
            return "s_"+uuid+"_"+fileName; // 이미지일 경우 섬네일
        }else{
            return uuid+"_"+fileName;
        }
    }
}
