package com.korit.silverbutton.dto.Matching.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingResponseDto {
    private Long dependentId;
    private Long caregiverId;

    public MatchingResponseDto(Matching matching) {
        this.dependentId = matching.getDependent().getId();
        this.caregiverId = matching.getCaregiver().getId();
    }
}