package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.MATCHING)
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // 모든 매칭 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> getAllMatchings(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long id = principalUser.getId();
        ResponseDto<List<MatchingResponseDto>> response = matchingService.getAllMatchings(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }


    // 특정 매칭 조회 요양사 특정인의 정보
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MatchingResponseDto>> getMatchingById(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        MatchingResponseDto matchingResponseDto = matchingService.getMatchingById(id, principalUser.getId());
        ResponseDto<MatchingResponseDto> response = matchingService.getMatchingById(id, matchingId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 매칭 삭제
    @DeleteMapping
    public ResponseEntity<ResponseDto<Void>> deleteMatching(
            @RequestParam Long id,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long userId = principalUser.getId();

        ResponseDto<Void> response = matchingService.deleteMatching(id, userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }
}
