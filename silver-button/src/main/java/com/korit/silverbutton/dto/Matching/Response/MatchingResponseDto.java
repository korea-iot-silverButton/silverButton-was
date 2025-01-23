package com.korit.silverbutton.dto.Matching.Response;

import com.korit.silverbutton.entity.Matchings;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingResponseDto {
    private Long dependentId;
    private Long caregiverId;

    public MatchingResponseDto(Matchings matchings) {
        this.dependentId = matchings.getId().getDependentId();
        this.caregiverId = matchings.getId().getCaregiverId();
    }
}