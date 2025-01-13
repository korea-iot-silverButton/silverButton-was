package com.korit.silverbutton.dto.boardlike.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardLikeRequestDto {
    private Long id;
    private Long boardId;
}
