package com.example.pracboard.domain.board.entity;

import com.example.pracboard.domain.member.entity.Member;
import com.example.pracboard.global.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "writer")
@Getter
public class Board extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    private long price;

    public void change(String title, long price, String content){
        this.title=title;
        this.price=price;
        this.content=content;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member writer;

}
