package com.korit.silverbutton.dto.Matching.Response;

import com.korit.silverbutton.entity.User;
import lombok.Getter;

@Getter
public class MatchingResponseDto {
    private final Long dependentId; // 필수
    private final Long caregiverId; // 필수

    private final String dependentName; // 선택
    private final String caregiverName; // 선택

    public MatchingResponseDto(Long dependentId, Long caregiverId, String dependentName, String caregiverName) {
        this.dependentId = dependentId;
        this.caregiverId = caregiverId;
        this.dependentName = dependentName;
        this.caregiverName = caregiverName;
    }

    public static MatchingResponseDto fromEntities(User caregiver, User dependent) {
        return new MatchingResponseDto(
                dependent.getId(),
                caregiver.getId(),
                dependent.getName(),
                caregiver.getName()
        );
    }
}

/*
불변성을 유지하기 위해 final을 적용.
@Getter: 게터만 생성.
@RequiredArgsConstructor: final 필드만 포함하는 생성자 자동 생성.
응답 DTO에서는 보통 데이터 생성 시 검증이 완료된 상태라고 가정하므로 제거. -- notnull
 */