package com.korit.silverbutton.dto.HealthMagazine.response;

import com.korit.silverbutton.entity.HealthMagazine;

import com.korit.silverbutton.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthMagazineResponseDto {

    private Long id;

    // 대표 이미지
    private String thumbnailImageUrl;

    // 제목
    private String title;

    // 내용
    private String content;

    // 작성일자
    private LocalDateTime publishedDate;

    // 출처
    private String source;

    // 조회수
    private int viewCount;


    public HealthMagazineResponseDto(HealthMagazine healthMagazine) {
        this.id = healthMagazine.getId();
        this.thumbnailImageUrl = healthMagazine.getThumbnailImageUrl();
        this.title = healthMagazine.getTitle();
        this.content = healthMagazine.getContent();
        this.publishedDate = healthMagazine.getPublishedDate();
        this.source = healthMagazine.getSource();
        this.viewCount = healthMagazine.getViewCount();
    }


}
