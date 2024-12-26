package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Message.Request.MessageRequestDto;
import com.korit.silverbutton.dto.Message.Response.MessageResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.repository.MessageRepository;
import com.korit.silverbutton.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;


    // 쪽지 전체 조회
    @Override
    public ResponseDto<List<MessageResponseDto>> getAllMessages(Long id) {

        try{
            List<Message> Messages = messageRepository.findMessageById(id);
            if (Messages.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);

            }
            // 메시지를 DTO로 변환
            List<MessageResponseDto> messageDtos = Messages.stream()
                    .map(MessageResponseDto::new) // MessageResponseDto 생성자 활용
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }


    // 쪽지 삭제
    @Override
    public ResponseDto<Void> deleteMessage(Long id) {

        try {
            Optional<Message> optionalMessage = messageRepository.findById(id);
            if (optionalMessage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            Message message = optionalMessage.get();
            messageRepository.delete(message);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    // 쪽지 작성
    @Override
    public ResponseDto<MessageResponseDto> createMessage(MessageRequestDto dto, Long id) {
        return null;
//        MessageResponseDto data = null;
//        String title = dto.getTitle();
//        String content = dto.getContent();
//        User senderId = dto.getSenderId();
//        User receiverId = dto.getReceiverId();
//
//        try{
//            Message message = Message.builder()
//                    .title(title)
//                    .content(content)
//                    .receiverId(receiverId)
//                    .senderId(senderId)
//                    .build();
//            messageRepository.save(message);
//
//            data = new MessageResponseDto(message);
//
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//
//        }
//
//        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

    }

    // 특정 쪽지 조회
    @Override
    public ResponseDto<MessageResponseDto> getMessageById(Long id) {
        MessageResponseDto data = null;
        Long messageId = id;

        try{
            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            if (optionalMessage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);

            }
            Message message = optionalMessage.get();
            data = new MessageResponseDto(message);


        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }


        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 발신 쪽지 조회
    @Override
    public ResponseDto<List<MessageResponseDto>> getOutGoingMessages(Long id) {
//        try {
//            List<Message> messages = messageRepository.findAllBySenderId();
//            if (messages.isEmpty()) {
//                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
//            }
//
//            List<MessageResponseDto> messageDtos = messages.stream()
//                    .map(MessageResponseDto::new)
//                    .collect(Collectors.toList());
//
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
        return null;
    }

    // 수신 쪽지 조회
    @Override
    public ResponseDto<List<MessageResponseDto>> getReceiveMessages(Long id) {
//        try {
//            List<Message> messages = messageRepository.findAllByReceiverId(id);
//            if (messages.isEmpty()) {
//                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
//            }
//
//            List<MessageResponseDto> messageDtos = messages.stream()
//                    .map(MessageResponseDto::new)
//                    .collect(Collectors.toList());
//
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//    }
        return null;
    }
}