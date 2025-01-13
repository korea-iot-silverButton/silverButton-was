package com.korit.silverbutton.dto.boardlike.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeResponseDto {
    private Long id;
    private Long boardId;
    private Long likerId;
    private int likes;

    public BoardLikeResponseDto(Long boardId, int likes) {
        this.boardId = boardId;
        this.likes = likes;
    }
}
