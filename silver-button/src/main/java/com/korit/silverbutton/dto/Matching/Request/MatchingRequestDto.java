package com.korit.silverbutton.dto.Matching.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingRequestDto {
    @NotNull(message = "Dependent ID는 필수 값입니다.")
    @Positive(message = "Dependent ID는 양수여야 합니다.")
    private Long dependentUserId;

    @NotNull(message = "Caregiver ID는 필수 값입니다.")
    @Positive(message = "Caregiver ID는 양수여야 합니다.")
    private Long caregiverUserId;
}

