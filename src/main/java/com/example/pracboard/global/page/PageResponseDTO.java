package com.example.pracboard.global.page;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<DTO, EN>{

    // dto 리스트
    private List<DTO> dtoList;

    /*page*/
    private int totalPage;
    private int page;
    private int size;

    /*nav*/
    private int start, end;
    private boolean prev, next;
    private List<Integer> pageList;

    public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }



    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1;
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        prev = start > 1;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }
}
