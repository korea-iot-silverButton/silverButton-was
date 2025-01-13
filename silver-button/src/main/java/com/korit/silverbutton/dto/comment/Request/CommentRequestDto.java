package com.korit.silverbutton.dto.comment.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull(message = "Board ID cannot be null")
    private Long boardId;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
