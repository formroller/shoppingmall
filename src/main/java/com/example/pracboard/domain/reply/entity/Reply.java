package com.example.pracboard.domain.reply.entity;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.global.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = "board")
public class Reply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rno;

    private String text;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private void changeText(String text){this.text=text;}
}
