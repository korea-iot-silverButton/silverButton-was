package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.boardlike.Request.BoardLikeRequestDto;
import com.korit.silverbutton.dto.boardlike.Response.BoardLikeResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.BoardLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.BOARD_LIKE)
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    private static final String TOGGLE_LIKE = "/toggle";

    @PostMapping(TOGGLE_LIKE)
    public ResponseEntity<ResponseDto<BoardLikeResponseDto>> toggleLike(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid BoardLikeRequestDto dto
    ) {
        if (dto.getBoardId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ResponseDto<BoardLikeResponseDto> response = boardLikeService.toggleLike(principalUser.getId(), dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}