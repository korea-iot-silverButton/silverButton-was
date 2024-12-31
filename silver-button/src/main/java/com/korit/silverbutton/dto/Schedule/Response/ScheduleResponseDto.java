package com.korit.silverbutton.dto.Schedule.Response;

import lombok.*;

@Data
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String scheduleDate;
    private String task;
}
