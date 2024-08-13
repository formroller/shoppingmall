package com.example.malls.domain.entity;

import com.example.malls.global.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Reply", indexes = {
        @Index(name="idx_reply_board_bno", columnList = "board_bno") // 쿼리 조건으로 자주 사용되는 칼럼은 인덱스 지정
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "board")
@Getter
public class Reply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_bno")
    private Board board;


    public void changeText(String text){
        this.replyText=text;
    }
}
