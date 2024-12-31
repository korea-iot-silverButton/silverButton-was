package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.Matching;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.MatchingRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;

    @Override
    public ResponseDto<List<MatchingResponseDto>> getAllMatchings(Long id) {
        try {
            List<Matching> matchings = matchingRepository.findAll();
            List<MatchingResponseDto> matchingDtos = matchings.stream()
                    .map(matching -> MatchingResponseDto.fromEntities(matching.getCaregiver(), matching.getDependent()))
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess("매칭 목록 조회 성공", matchingDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("매칭 목록 조회 중 오류가 발생했습니다.");
        }
    }

    @Override
    public ResponseDto<MatchingResponseDto> getMatchingById(Long id) {
        try{
            Matching matching = matchingRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matching not found with id: " + id));

            MatchingResponseDto matchingDto = MatchingResponseDto.fromEntities(matching.getCaregiver(), matching.getDependent());
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, matchingDto);
        } catch (ResponseStatusException e) {
            return ResponseDto.setFailed(e.getReason());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<Void> deleteMatching(Long id) {

        try {
            Optional<Matching> matching = matchingRepository.findById(id);
            if (matching.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            matchingRepository.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<MatchingResponseDto> createMatching(MatchingRequestDto dto, Long userId) {

        MatchingResponseDto data = null;

        try {
            Long dependentId = dto.getDependentUserId();
            Long caregiverId = dto.getCaregiverUserId();

            User dependent = userRepository.findById(dependentId).orElse(null);
            User caregiver = userRepository.findById(caregiverId).orElse(null);

            Matching matching = Matching.builder()
                    .dependent(dependent)
                    .caregiver(caregiver)
                    .build();
            matching = matchingRepository.save(matching);

            data = new MatchingResponseDto(
                    matching.getDependent().getId(),
                    matching.getCaregiver().getId(),
                    matching.getDependent().getName(),
                    matching.getCaregiver().getName()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<String> getUserRole(String userId) {
        try {
            // Optional로 반환된 값 처리
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            // 역할 정보 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, user.getRole());
        } catch (ResponseStatusException e) {
            return ResponseDto.setFailed(e.getReason());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public void validateUserRole(Long userId, String requiredRole) {
        try {
            // 사용자 역할 조회
            ResponseDto<String> response = getUserRole(userId.toString());
            if (!response.isResult() || !requiredRole.equals(response.getData())) {
                throw new AccessDeniedException("해당 작업에 대한 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AccessDeniedException("권한 검증 중 오류가 발생했습니다.", e);
        }
    }

}
