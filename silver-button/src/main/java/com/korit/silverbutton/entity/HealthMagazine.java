package com.korit.silverbutton.entity;

import ch.qos.logback.core.testUtil.MockInitialContext;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "health_magazine")
@Builder
public class HealthMagazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 대표 이미지
    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    // 제목
    @Column(name = "title")
    private String title;

    // 내용
    @Column(name = "content")
    private String content;

    // 작성일자
    @Column(name = " published_date")
    private LocalDateTime publishedDate;

    // 출처
    @Column(name = "source")
    private String source;

    // 조회수
    @Column(name = "view_count")
    private int viewCount;

    // 작성일자
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
