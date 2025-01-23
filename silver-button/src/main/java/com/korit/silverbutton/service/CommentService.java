package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.comment.Request.CommentRequestDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import jakarta.validation.Valid;

import java.util.List;

public interface CommentService {

    ResponseDto<CommentResponseDto> createComment(PrincipalUser name, PrincipalUser phone, @Valid CommentRequestDto dto);

    ResponseDto<List<CommentResponseDto>> getAllComments(Long boardId);

    ResponseDto<Void> deleteComment(Long userId, Long id);
}
