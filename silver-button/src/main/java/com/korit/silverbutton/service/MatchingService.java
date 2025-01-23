package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Response.PartnerProfileDto;
import com.korit.silverbutton.entity.User;

import java.util.List;

public interface MatchingService {
    ResponseDto<List<MatchingResponseDto>> getAllMatchings();

    Boolean getMatchingById(Long userId);

    // 매칭된 상대의 프로필 조회
    ResponseDto<PartnerProfileDto> getPartner(Long userId, String role);

    ResponseDto<Void> deleteMatching(Long id, Long userId);

    ResponseDto<MatchingResponseDto> createMatching(Long caregiverId, Long dependentId);

    ResponseDto<List<User>> contractablecaregiver(Long id);
}
