package com.example.pracboard.domain.product.entity;

import com.example.pracboard.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String tag;

    private String title;
    private String price;
    private String context;
    private int qty;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
