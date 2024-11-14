package com.korit.silverbutton.dto.Schedule.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleRequestDto {
    @NotBlank
    private Long dependentId;

    @NotBlank
    private int year;

    @NotBlank
    private int month;
}
