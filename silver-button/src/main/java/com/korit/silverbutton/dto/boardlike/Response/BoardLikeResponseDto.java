package com.korit.silverbutton.dto.boardlike.Response;


import com.korit.silverbutton.dto.board.Response.BoardResponseDto;
import com.korit.silverbutton.entity.BoardLike;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardLikeResponseDto extends BoardResponseDto {
    private Long id;
    private Long boardId;
    private Long likerId;
    private int likes;

    public BoardLikeResponseDto(BoardLike boardLike) {
        this.id = boardLike.getId();
        this.boardId = boardLike.getBoard().getId();
        this.likerId = boardLike.getLiker().getId();
        this.likes = boardLike.getBoard().getLikes();
    }

    public BoardLikeResponseDto(Long boardId, int likes) {
        this.boardId = boardId;
        this.likes = likes;
    }
}
