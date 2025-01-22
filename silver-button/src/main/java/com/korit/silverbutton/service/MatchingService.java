package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;

import java.util.List;

public interface MatchingService {
    ResponseDto<List<MatchingResponseDto>> getAllMatchings();

    Boolean getMatchingById(Long userId);

    ResponseDto<Void> deleteMatching(Long id, Long userId);

    ResponseDto<MatchingResponseDto> createMatching(Long caregiverId, Long dependentId);

    ResponseDto<List<User>> contractablecaregiver(Long id);
}
