package com.korit.silverbutton.dto.Message.Response;

import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MessageResponseDto {
    private final Long id;
    private final Long senderId;
    private final String senderName;
    private final Long receiverId;
    private final String receiverName;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.senderName = message.getSender().getName();
        this.receiverId = message.getReceiver().getId();
        this.receiverName = message.getReceiver().getName();
        this.createdAt = message.getCreatedAt();
        this.title = message.getTitle();
        this.content = message.getContent();
    }
}

