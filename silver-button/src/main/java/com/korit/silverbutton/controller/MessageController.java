package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.Message.Request.MessageRequestDto;
import com.korit.silverbutton.dto.Message.Response.MessageResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.MESSAGE)
@RequiredArgsConstructor
public class MessageController {



    private final MessageService messageService;
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    //  모든 쪽지 조회 - 확인 완료
    @GetMapping
    public ResponseEntity<ResponseDto<List<MessageResponseDto>>> getAllMessages(
            @AuthenticationPrincipal PrincipalUser principalUser
    )
    {
        {
            if (principalUser == null) {
                log.error("PrincipalUser is null. Authentication might have failed.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        Long id = principalUser.getId();
        ResponseDto<List<MessageResponseDto>> response = messageService.getAllMessages(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 발신 쪽지 조회 - 확인 완료
    @GetMapping("/sender")
    public ResponseEntity<ResponseDto<List<MessageResponseDto>>> getOutGoingMessages(
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        Long id = principalUser.getId();
        ResponseDto<List<MessageResponseDto>> response = messageService.getOutGoingMessages(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 수신 쪽지 조회 - 확인 완료
    @GetMapping("/receive")
    public ResponseEntity<ResponseDto<List<MessageResponseDto>>> getReceiveMessages(
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        Long id = principalUser.getId();
        ResponseDto<List<MessageResponseDto>> response = messageService.getReceiveMessages(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 쪽지 작성 - 확인 완료
    @PostMapping
    public ResponseEntity<ResponseDto<MessageResponseDto>> createMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long senderId = principalUser.getId();
        ResponseDto<MessageResponseDto> response = messageService.createMessage(messageRequestDto, senderId);
        HttpStatus status = response.isResult() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    // 쪽지 삭제 - 완료
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteMessage(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long userId = principalUser.getId();

        ResponseDto<Void> response = messageService.deleteMessage(id, userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 특정 쪽지 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MessageResponseDto>> getMessageById(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        Long userId = principalUser.getId();

        ResponseDto<MessageResponseDto> response = messageService.getMessageById(id, userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }
}
