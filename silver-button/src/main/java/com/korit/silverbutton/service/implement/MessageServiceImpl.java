package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Message.Request.MessageRequestDto;
import com.korit.silverbutton.dto.Message.Response.MessageResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.MessageRepository;
import com.korit.silverbutton.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 쪽지 전체 조회 - 완료
    @Override
    public ResponseDto<List<MessageResponseDto>> getAllMessages(Long id) {

        try{
            List<Message> messages = messageRepository.findMessageById(id);
            if (messages.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);

            }
            // 메시지를 DTO로 변환
            List<MessageResponseDto> messageDtos = messages.stream()
                    .map(MessageResponseDto::new) // MessageResponseDto 생성자 활용
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 쪽지 작성 - 완료
    @Override
    public ResponseDto<MessageResponseDto> createMessage(MessageRequestDto dto, Long senderId) {
        MessageResponseDto data = null;

        String title = dto.getTitle();
        String content = dto.getContent();

        try{
            // User ID를 이용해 User 객체를 데이터베이스에서 조회
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));
            User receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid receiver ID"));

            // Message 엔티티 생성
            Message message = Message.builder()
                    .title(title)
                    .content(content)
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            messageRepository.save(message);

            data = new MessageResponseDto(message);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);

        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }



    // 발신 쪽지 조회 - 완료
    @Override
    public ResponseDto<List<MessageResponseDto>> getOutGoingMessages(Long senderId) {
        User sender = new User();
        sender.setId(senderId);
        try {
            List<Message> messages = messageRepository.findAllBySender(sender);
            if (messages.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<MessageResponseDto> messageDtos = messages.stream()
                    .map(MessageResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

    }

    // 수신 쪽지 조회 - 완료
    @Override
    public ResponseDto<List<MessageResponseDto>> getReceiveMessages(Long receiverId) {
        User receiver = new User();
        receiver.setId(receiverId);
        try {
            List<Message> messages = messageRepository.findAllByReceiver(receiver);
            if (messages.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            List<MessageResponseDto> messageDtos = messages.stream()
                    .map(MessageResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, messageDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 쪽지 삭제 - 완료
    @Override
    public ResponseDto<Void> deleteMessage(Long messageId, Long userId) {
        try {
            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            if (optionalMessage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }

            Message message = optionalMessage.get();

            // 로그인한 사용자가 발신자 또는 수신자인지 확인
            boolean isAuthorized = message.getSender().getId().equals(userId)
                    || message.getReceiver().getId().equals(userId);

            if (!isAuthorized) {
                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED); // 권한 없음
            }

            messageRepository.delete(message); // 삭제 수행

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    // 특정 쪽지 조회 - 완료
    @Override
    public ResponseDto<MessageResponseDto> getMessageById(Long messageId, Long userId) {
        try{
            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            if (optionalMessage.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);

            }
            Message message = optionalMessage.get();

            // 권한 검사
            if (!message.getSender().getId().equals(userId) && !message.getReceiver().getId().equals(userId)) {
                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED);
            }

            // 메시지 조회 성공
            MessageResponseDto data = new MessageResponseDto(message);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    }




