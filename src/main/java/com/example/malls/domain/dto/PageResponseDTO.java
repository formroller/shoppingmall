package com.example.malls.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;

    // 페이지 시작, 끝 번호
    private int start;
    private int end;

    // 이전 페이지 여부
    private boolean prev;
    private boolean next;


    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        if(total <= 0){
            return;
        }

        this.page=pageRequestDTO.getPage();
        this.size=pageRequestDTO.getSize();

        this.total=total;
        this.dtoList=dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면 마지막 번호
        this.start = this.end -9;
//
//        this.start = this.end - 9;

        int last = (int)(Math.ceil((total/(double)size))); // 마지막 번호

        this.end = end > last ? last : end;

        this.prev = this.start>1;

        this.next = total > this.end *this.size;
    }
}
