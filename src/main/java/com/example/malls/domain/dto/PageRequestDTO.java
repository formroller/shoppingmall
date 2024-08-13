package com.example.malls.domain.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int page=1;
    @Builder.Default
    private int size=12;

    private String keyword;
    private String link;

    public Pageable getPageable(String...props){
        return PageRequest.of(this.page-1, size, Sort.by(props).descending());
    }

    public String getLink(){
        if(link==null){
            StringBuilder builder = new StringBuilder();

            builder.append("page="+this.page);
            builder.append("&size="+this.size);

            if(keyword != null){
                try{
                    builder.append("&keyword="+ URLEncoder.encode(keyword, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                }
            }
            link = builder.toString();
        }
        return link;
    }
}
