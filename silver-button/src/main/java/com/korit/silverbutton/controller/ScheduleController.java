package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Request.ScheduleCreateRequestDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleCreateResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;
import com.korit.silverbutton.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleservice;

    @GetMapping("/search")
    private ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> findSchedulesByDependentIdAndDate
            (@RequestParam int year, @RequestParam int month, @AuthenticationPrincipal String userId){
        ResponseDto<List<ScheduleResponseDto>> result=
                scheduleservice.getScheduleByDependentIdAndDate(userId, year, month);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 자신이 피부양자일 경우 스케줄 스스로 추가
    @PostMapping("/create")
    private ResponseEntity<ResponseDto<ScheduleCreateResponseDto>> createScedule(
            @Valid @RequestBody ScheduleCreateRequestDto dto){
        ResponseDto<ScheduleCreateResponseDto> result= scheduleservice.createScheduleSelf(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
