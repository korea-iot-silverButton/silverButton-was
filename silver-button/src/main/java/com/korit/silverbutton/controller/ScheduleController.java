package com.korit.silverbutton.controller;

import com.korit.silverbutton.dto.Request.ScheduleRequestDto;
import com.korit.silverbutton.dto.Response.ResponseDto;
import com.korit.silverbutton.dto.Response.ScheduleResponseDto;
import com.korit.silverbutton.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class ScheduleController {
    @PostMapping
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> createSchedule(@RequestBody ScheduleRequestDto dto){
        ResponseDto<ScheduleResponseDto> result= ScheduleService.createSchedule(dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("{dependent_id}")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getAllSchedule(){

    }
    @PutMapping("/{dependent_id}/date/{datetime}")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>>updatePost(){
        
    }
}
