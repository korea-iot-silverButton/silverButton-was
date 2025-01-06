package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.boardlike.Request.BoardLikeRequestDto;
import com.korit.silverbutton.dto.boardlike.Response.BoardLikeResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.BoardLikeService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    private static final String TOGGLE_LIKE = "/boardlike/toggle"; // 좋아요 토글 경로

    @PostMapping(TOGGLE_LIKE)
    @PermitAll
    public ResponseEntity<ResponseDto<BoardLikeResponseDto>> toggleLike(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid BoardLikeRequestDto dto
    ) {
        if (dto.getBoardId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 로그인된 사용자 ID 또는 테스트용 1L
        Long userId = principalUser != null ? principalUser.getId() : 1L;
        System.out.println("Received DTO: " + dto);

        // 좋아요 추가 또는 삭제 처리
        ResponseDto<BoardLikeResponseDto> response = boardLikeService.toggleLike(userId, dto);
        System.out.println("Toggle Like Response: " + response.getData());

        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}