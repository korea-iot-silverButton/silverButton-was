package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;
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

    // 모든 매칭 조회 - 완료
    @GetMapping("/matching-all")
    public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> getAllMatchings(
    ) {
        ResponseDto<List<MatchingResponseDto>> response = matchingService.getAllMatchings();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    } // id로만 출력 되는 것 수정해야 함

    @GetMapping("/search-caregiver")
    public ResponseEntity<ResponseDto<List<User>>> getAllContractAble(
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        ResponseDto<List<User>> response= matchingService.contractablecaregiver(principalUser.getId());
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 특정 매칭 조회- 완료
    @GetMapping("matching-any")
    public ResponseEntity<ResponseDto<MatchingResponseDto>> getMatchingById(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<MatchingResponseDto> responseDto = matchingService.getMatchingById(principalUser.getId());
        HttpStatus status = responseDto.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseDto);
    }

    //매칭 하기
    @PostMapping("/matching-make")
    public ResponseEntity<ResponseDto<MatchingResponseDto>> createMatching(
            @RequestBody @Valid MatchingRequestDto dto,
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long id = principalUser.getId();

        try{
            ResponseDto<MatchingResponseDto> response = matchingService.createMatching(dto, id);
            HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.setFailed("Internal server error"));
        }
    }

    // 매칭 삭제 - 완료
    @DeleteMapping("/matching-delete")
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
