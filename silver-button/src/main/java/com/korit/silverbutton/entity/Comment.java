package com.korit.silverbutton.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference  // 순환 참조 방지를 위한 어노테이션
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer",referencedColumnName = "id" ,nullable = false)
    private User writer;

    @Column(nullable = false, length = 200)
    private String content;

    @Override
    public String toString() {
        return "Comment{id=" + id + ", content='" + content + "', writer=" + writer + "}";
    }
}
