package com.korit.silverbutton.dto.board.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @Size(max = 2083, message = "Image URL length must be less than 2083 characters")
    private String imageUrl; // Optional field for image path or URL


}
