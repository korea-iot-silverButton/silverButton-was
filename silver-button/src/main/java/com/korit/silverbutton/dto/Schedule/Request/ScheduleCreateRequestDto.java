package com.korit.silverbutton.dto.Schedule.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequestDto {
    private Long dependentId;
    private Date scheduleDate;
    private String task;
}
