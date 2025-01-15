package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.Matching;
import com.korit.silverbutton.entity.Message;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.MatchingRepository;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;

    //매칭 전체 조회 ? - 완료지만 필요가 할까 ? ? ? 관리자만 볼수 있게 ?
    @Override
    public ResponseDto<List<MatchingResponseDto>> getAllMatchings(Long id) {
        try {
            List<Matching> matchings = matchingRepository.findAll();

            List<MatchingResponseDto> matchingDtos = matchings.stream()
                    .map(matching -> {
                        User caregiver = matching.getCaregiver();
                        User dependent = matching.getDependent();

                        // MatchingResponseDto 직접 생성
                        return new MatchingResponseDto(
                                dependent.getId(),
                                caregiver.getId(),
                                dependent.getName(),
                                caregiver.getName(),
                                dependent.getPhone(), // 필요하면 dependent.getPhone() 사용
                                dependent.getEmail(),
                                dependent.getBirthDate(), // Date 타입
                                dependent.getGender(),
                                dependent.getProfileImage()
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess("매칭 목록 조회 성공", matchingDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("매칭 목록 조회 중 오류가 발생했습니다.");
        }
    }

    @Override
    public ResponseDto<List<MatchingResponseDto>> getMatchingById(Long id) {
        try {

            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
            // caregiver_id 또는 dependent_id와 일치하는 매칭 가져오기
            List<Matching> matchings = matchingRepository.findAllByCaregiverUserIdOrDependentUserId(currentUserId);

            // MatchingResponseDto 리스트로 변환
            List<MatchingResponseDto> matchingDtos = matchings.stream()
                    .map(matching -> {
                        User caregiver = matching.getCaregiver();
                        User dependent = matching.getDependent();

                        return new MatchingResponseDto(
                                dependent.getId(),
                                caregiver.getId(),
                                dependent.getName(),
                                caregiver.getName(),
                                dependent.getPhone(),
                                dependent.getEmail(),
                                dependent.getBirthDate(),
                                dependent.getGender(),
                                dependent.getProfileImage()
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess("매칭 목록 조회 성공", matchingDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("매칭 목록 조회 중 오류가 발생했습니다.");
        }
    }

    //매칭 삭제
    @Override
    public ResponseDto<Void> deleteMatching(Long matchingId, Long userId) {
        return null;
//        try {
//            // 매칭 정보 조회
//            Optional<Matching> optionalMatching = matchingRepository.findById(matchingId);
//
//            if (optionalMatching.isEmpty()) {
//                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
//            }
//
//            Matching matching = optionalMatching.get();
//
//            // 권한 확인
//            boolean isAuthorized = matching.getDependent().getId().equals(userId)
//                    || matching.getCaregiver().getId().equals(userId);
//
//            if (!isAuthorized) {
//                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED); // 권한 없음
//            }
//
//            // 매칭 삭제
//            matchingRepository.delete(matching);
//
//            // 성공 응답 반환
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
//
//        } catch (Exception e) {
//            // 예외 처리
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
    }


    // 매칭 생성
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
                    dependent.getId(),
                    caregiver.getId(),
                    dependent.getName(),
                    caregiver.getName(),
                    caregiver.getPhone(),
                    caregiver.getEmail(),
                    caregiver.getBirthDate(), // birthDate는 Date 타입이어야 합니다.
                    caregiver.getGender(),
                    caregiver.getProfileImage()
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
