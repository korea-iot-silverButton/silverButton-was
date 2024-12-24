package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.comment.Request.CommentRequestDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface CommentService {

    ResponseDto<CommentResponseDto> createComment(String userId, @Valid CommentRequestDto dto);

    ResponseDto<List<CommentResponseDto>> getAllComments();

    ResponseDto<Void> deleteComment(String userId, Long id);
}