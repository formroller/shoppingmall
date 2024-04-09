package com.example.pracboard.domain.reply.entity;

import com.example.pracboard.domain.board.entity.Board;
import com.example.pracboard.global.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "board")
@Getter
public class Reply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rno;

    private String text;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
