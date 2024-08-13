package com.example.malls.domain.entity;

import com.example.malls.global.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
@Getter
public class Board extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;
    private long price;

    private String writer;

    public void change(String title, String content, long price){
        this.title=title;
        this.content=content;
        this.price=price;
    }


    // Board Image
    @OneToMany(mappedBy = "board",
            cascade = CascadeType.ALL, // 영속성 전이(ALL) - 상위 엔티티 모든 변경이 하위 엔티티 적용
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }

    public void clearImage(){
        if(imageSet != null){
            imageSet.forEach(boardImage -> boardImage.changeBoard(null));
            this.imageSet.clear();
        }
    }

}
