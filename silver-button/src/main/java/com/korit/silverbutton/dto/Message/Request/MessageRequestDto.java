package com.korit.silverbutton.dto.Message.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequestDto {
    private Long id;
    @NotNull
    private Long senderId;
    @NotNull
    private Long receiverId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
//    private String userId;
}