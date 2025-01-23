package com.korit.silverbutton.dto.comment.Response;

import com.korit.silverbutton.entity.Comment;
import lombok.Data;

@Data
public class CommentResponseDto {

    private Long id;
    private Long boardId;
    private String writer;
    private String content;
    private Long writerId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.boardId = (comment.getBoard() != null) ? comment.getBoard().getId() : null;
        this.writer = (comment.getWriter() != null) ? comment.getWriter().getName() : null;
        this.content = comment.getContent();
        this.writerId = (comment.getWriter() != null) ? comment.getWriter().getId() : null;
    }
}
