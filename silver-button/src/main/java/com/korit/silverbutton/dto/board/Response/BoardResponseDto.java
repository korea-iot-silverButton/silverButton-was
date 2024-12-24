package com.korit.silverbutton.dto.board.Response;

import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private Long user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private int views;
    private List<CommentResponseDto> comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.user = board.getUser().getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = LocalDateTime.now();
        this.likes = board.getLikes();
        this.views = board.getViews();
        this.comments = (board.getComments() != null) ?
                board.getComments().stream()
                        .map(CommentResponseDto::new)
                        .collect(Collectors.toList()):
                Collections.emptyList();
    }
}