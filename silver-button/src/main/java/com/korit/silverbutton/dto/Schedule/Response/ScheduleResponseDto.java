package com.korit.silverbutton.dto.Schedule.Response;

import com.korit.silverbutton.dto.Schedule.ScheduleList;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleResponseDto {
    private String scheduleDate;
    private String task;
}
