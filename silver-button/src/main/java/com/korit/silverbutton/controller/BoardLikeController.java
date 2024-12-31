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
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    private static final String INSERT = "/boardlike/insert";
    private static final String DELETE = "/boardlike/delete/{id}";

    @PostMapping(INSERT)
    public ResponseEntity<ResponseDto<BoardLikeResponseDto>> insertLike(
//            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid BoardLikeRequestDto dto
    ) {
//        ResponseDto<BoardLikeResponseDto> response = boardLikeService.insertLike(principalUser.getId(), dto);
        Long tempAuthorId = 1L;
        ResponseDto<BoardLikeResponseDto> response = boardLikeService.insertLike(tempAuthorId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<ResponseDto<BoardLikeResponseDto>> deleteLike(
//            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long id
    ) {
//        ResponseDto<BoardLikeResponseDto> response = boardLikeService.deleteLike(principalUser.getId(), id);
        Long testUserId = 1L;
        ResponseDto<BoardLikeResponseDto> response = boardLikeService.deleteLike(testUserId, id);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
