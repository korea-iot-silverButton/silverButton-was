package com.korit.silverbutton.dto.board.Response;

import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private Long user;
    private String username;
    private String title;
    private String content;
    private String imageUrl; // 업로드된 이미지 URL
    private LocalDateTime createdAt;
    private int likes;
    private int views;
    private List<CommentResponseDto> comments;
    private Long currentUserId;  // 현재 로그인 사용자 ID



    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.user = board.getUser().getId();
        this.username = board.getUser().getName();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.createdAt = board.getCreatedAt();
        this.likes = board.getLikes();
        this.views = board.getViews();
        this.comments = (board.getComments() != null) ?
                board.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()):
                Collections.emptyList();
        this.currentUserId = null;  // 현재 로그인 사용자 ID



    }

}
