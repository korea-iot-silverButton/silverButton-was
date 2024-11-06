package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.BoardRequestDto;
import com.korit.silverbutton.dto.BoardResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMappingPattern.BOARD)
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // board 생성
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(
            @Valid @RequestBody BoardRequestDto Dto,
            @AuthenticationPrincipal String Email) {

        ResponseDto<BoardResponseDto> result = boardService.createboard
    }

}
