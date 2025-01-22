package com.korit.silverbutton.dto.Matching.Response;

import com.korit.silverbutton.entity.Matching;
import lombok.*;

import java.util.Date;

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