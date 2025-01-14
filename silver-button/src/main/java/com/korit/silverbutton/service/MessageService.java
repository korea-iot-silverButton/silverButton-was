package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Message.Request.MessageRequestDto;
import com.korit.silverbutton.dto.Message.Response.MessageResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;

import java.util.List;

public interface MessageService {
    ResponseDto<List<MessageResponseDto>> getAllMessages(Long id);


    ResponseDto<Void> deleteMessage(Long id, Long userId);

    ResponseDto<MessageResponseDto> createMessage(MessageRequestDto dto, Long id);

    ResponseDto<MessageResponseDto> getMessageById(Long messageId, Long userId);

    ResponseDto<List<MessageResponseDto>> getOutGoingMessages(Long id);

    ResponseDto<List<MessageResponseDto>> getReceiveMessages(Long id);
}
