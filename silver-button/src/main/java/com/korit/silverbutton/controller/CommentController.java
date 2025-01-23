package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.comment.Request.CommentRequestDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.COMMENT)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private static final String POST_COMMENT = "/create";
    private static final String GET_COMMENT_ALL = "/all";
    private static final String DELETE_COMMENT = "/delete/{id}";


    @PostMapping(POST_COMMENT)
    public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(
            @Valid @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<CommentResponseDto> response
                = commentService.createComment(principalUser, principalUser, dto);
        HttpStatus status
                = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(GET_COMMENT_ALL)
    public ResponseEntity<ResponseDto<List<CommentResponseDto>>> getAllComments(
            @RequestParam Long boardId
    ) {
        ResponseDto<List<CommentResponseDto>> response
                = commentService.getAllComments(boardId);
        HttpStatus status
                = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(DELETE_COMMENT)
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
        ResponseDto<Void> response
                = commentService.deleteComment(principalUser.getId(), id);
        HttpStatus status
                = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}