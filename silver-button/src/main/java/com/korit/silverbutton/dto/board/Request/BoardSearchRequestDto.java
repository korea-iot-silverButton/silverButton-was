package com.korit.silverbutton.dto.board.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardSearchRequestDto {
    private String keyword;    // 제목 키워드
    private String author;     // 작성자 이름 또는 I
}
