package com.korit.silverbutton.dto.Request;

import lombok.Data;

@Data
public class ScheduleRequestDto {
    private String dependent_id;
    private String schedule_date;
    private String task;
}
