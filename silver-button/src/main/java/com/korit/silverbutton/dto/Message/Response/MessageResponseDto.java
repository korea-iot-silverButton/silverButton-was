package com.korit.silverbutton.dto.Message.Response;

import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MessageResponseDto {
    private final User senderId;
    private final User receiverId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    // Message 엔티티를 기반으로 DTO를 생성하는 생성자
    public MessageResponseDto(Message message) {
        this.senderId = message.getSenderId();
        this.receiverId = message.getReceiverId();
        this.createdAt = message.getCreatedAt();
        this.title = message.getTitle();
        this.content = message.getContent();
    }
}

