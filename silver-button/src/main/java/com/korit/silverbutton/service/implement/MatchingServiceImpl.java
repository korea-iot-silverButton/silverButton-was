package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Matching.Request.MatchingRequestDto;
import com.korit.silverbutton.dto.Matching.Response.MatchingResponseDto;
import com.korit.silverbutton.dto.Message.Response.MessageResponseDto;
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
                                caregiver.getPhone(), // 필요하면 dependent.getPhone() 사용
                                caregiver.getEmail(),
                                caregiver.getBirthDate(), // Date 타입
                                caregiver.getGender(),
                                caregiver.getProfileImage()
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseDto.setSuccess("매칭 목록 조회 성공", matchingDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("매칭 목록 조회 중 오류가 발생했습니다.");
        }
    }

    //특정 매칭 조회 - 완료
    @Override
    public ResponseDto<MatchingResponseDto> getMatchingById(Long matchingId, Long userId) {
        try {

            // id와 사용자 ID가 일치하는 단일 매칭 가져오기
            Matching matching = matchingRepository.findById(matchingId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "매칭을 찾을 수 없습니다."));

            User caregiver = matching.getCaregiver();
            User dependent = matching.getDependent();

            // 권한 검사
            if (!matching.getCaregiver().getId().equals(userId) && !matching.getDependent().getId().equals(userId)) {
                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED);
            }

            // MatchingResponseDto 리스트로 변환
            MatchingResponseDto matchingResponseDto = new MatchingResponseDto(
                    dependent.getId(),
                    caregiver.getId(),
                    dependent.getName(),
                    caregiver.getName(),
                    caregiver.getPhone(),
                    caregiver.getEmail(),
                    caregiver.getBirthDate(),
                    caregiver.getGender(),
                    caregiver.getProfileImage()
            );

            return ResponseDto.setSuccess("매칭 목록 조회 성공", matchingResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("매칭 목록 조회 중 오류가 발생했습니다.");
        }
    }

    //매칭 삭제 - 완료
    @Override
    public ResponseDto<Void> deleteMatching(Long id, Long userId) {
        try {
            // 매칭 정보 조회
            Optional<Matching> optionalMatching = matchingRepository.findById(id);

            if (optionalMatching.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
            Matching matching = optionalMatching.get();

            // 권한 확인
            boolean isAuthorized = matching.getDependent().getId().equals(userId)
                    || matching.getCaregiver().getId().equals(userId);

            if (!isAuthorized) {
                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED); // 권한 없음
            }

            // 매칭 삭제
            matchingRepository.delete(matching);

            // 성공 응답 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }


    // 매칭 생성
    @Override
    public ResponseDto<MatchingResponseDto> createMatching(MatchingRequestDto dto, Long userId) {
        return null;
//        MatchingResponseDto data = null;
//
//        try {
//            User dependent = userRepository.findById(dto.getDependentUserId()).orElse(null);
//            User caregiver = userRepository.findById(dto.getCaregiverUserId()).orElse(null);
//
//            // 중복 매칭 여부 확인
//            boolean exists = matchingRepository.existsByDependentIdAndCaregiverId(dependent.getId(), caregiver.getId());
//            if (exists) {
//                return ResponseDto.setFailed("Matching already exists.");
//            }
//
//            Matching matching = Matching.builder()
//                    .dependent(dependent)
//                    .caregiver(caregiver)
//                    .build();
//
//            matchingRepository.save(matching);
//
//            data = new MatchingResponseDto(matching);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

}
