package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;
import com.korit.silverbutton.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleservice;

    @GetMapping("/search")
    private ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> findSchedulesByDependentIdAndDate
            (@RequestParam Long dependentId, @RequestParam int year, @RequestParam int month){
        ResponseDto<List<ScheduleResponseDto>> result=
                scheduleservice.getScheduleByDependentIdAndDate(dependentId, year, month);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
