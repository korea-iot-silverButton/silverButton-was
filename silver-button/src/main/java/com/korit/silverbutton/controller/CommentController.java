package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.comment.Request.CommentRequestDto;
import com.korit.silverbutton.dto.comment.Response.CommentResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.CommentService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private static final String COMMENT = "/comment";
    private static final String COMMENT_ALL = "/comment/all";
    private static final String COMMENT_DELETE = "/comment/{id}";


    @PostMapping(COMMENT)
    @PermitAll
    public ResponseEntity<ResponseDto<CommentResponseDto>>  createComment(
            @Valid @RequestBody CommentRequestDto dto
//            @AuthenticationPrincipal PrincipalUser principalUser

    ){

//        ResponseDto<CommentResponseDto> reponse = commentService.createComment(principalUser.getId(), dto);
        Long tempAuthorId = 1L;
        ResponseDto<CommentResponseDto> reponse = commentService.createComment(tempAuthorId, dto);
        HttpStatus status = reponse.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(reponse);
    }

    @GetMapping(COMMENT_ALL)
    @PermitAll
    public ResponseEntity<ResponseDto<List<CommentResponseDto>>> getAllComments(
    ) {
        ResponseDto<List<CommentResponseDto>> response = commentService.getAllComments();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(COMMENT_DELETE)
    @PermitAll
    public ResponseEntity<ResponseDto<Void>> deleteComment(
//            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
//        ResponseDto<Void> reponse = commentService.deleteComment(principalUser.getUserId(), id);
        Long tempAuthorId = 1L;
        ResponseDto<Void> reponse = commentService.deleteComment(tempAuthorId, id);
        HttpStatus status = reponse.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(reponse);
    }


}
