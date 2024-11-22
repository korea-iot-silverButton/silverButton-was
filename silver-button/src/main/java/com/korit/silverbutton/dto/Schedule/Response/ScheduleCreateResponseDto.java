package com.korit.silverbutton.dto.Schedule.Response;

import com.korit.silverbutton.entity.Schedules;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreateResponseDto {
    private Long id;
    private Long dependentId;
    private Date scheduleDate;
    private String task;

    public ScheduleCreateResponseDto(Schedules schedule) {
        this.id= schedule.getId();
        this.dependentId= schedule.getDependentId();
        this.scheduleDate= schedule.getScheduleDate();
        this.task= schedule.getTask();
    }
}
