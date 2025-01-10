package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;

import java.util.List;

public interface MatchingService {
    ResponseDto<List<MatchingResponseDto>> getAllMatchings(Long id);

    ResponseDto<MatchingResponseDto> getMatchingById(Long id);

    ResponseDto<Void> deleteMatching(Long id);

    ResponseDto<MatchingResponseDto> createMatching(MatchingRequestDto dto, Long id);

    ResponseDto<String> getUserRole(String userId);

    void validateUserRole(Long userId, String requiredRole);


}
