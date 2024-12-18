package com.korit.silverbutton.dto.comment.Response;

import com.korit.silverbutton.entity.Board;
import com.korit.silverbutton.entity.Comment;
import lombok.Data;

@Data
public class CommentResponseDto {

    private Long id;
    private Long boardId;     // 게시글
    private String writer;    // 작성자
    private String content;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.boardId = (comment.getBoard() != null) ? comment.getBoard().getId() : null ;
        this.writer = comment.getWriter();
        this.content = comment.getContent();
    }
}