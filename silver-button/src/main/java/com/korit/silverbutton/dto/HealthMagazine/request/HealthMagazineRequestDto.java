package com.korit.silverbutton.dto.HealthMagazine.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthMagazineRequestDto {
    // 아이디
    private Long id;

    // 대표 이미지
    private String thumbnailImageUrl;

    // 제목
    private String title;

    // 내용
    private String content;

    // 출처
    private String source;

    // 작성일자
    private LocalDateTime publishedDate;

    // 조회수
    private int viewCount;

}
