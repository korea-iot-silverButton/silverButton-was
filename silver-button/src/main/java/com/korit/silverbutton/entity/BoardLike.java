package com.korit.silverbutton.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "board_like")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private  Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id",referencedColumnName = "id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User liker;

}
