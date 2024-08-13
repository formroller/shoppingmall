package com.example.malls.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{ // 첨부파일

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    @JoinColumn(name="board_bno")
    private Board board;



    @Override
    public int compareTo(BoardImage other) {
        // OneToMany 처리에서 순번에 맞는 정렬 위해 사용
        return this.ord - other.ord;
    }

    public void changeBoard(Board board){
        // 추후 Board 삭제 시 BoardImage 객체 참조도 변경 위함
        this.board = board;
    }
}
