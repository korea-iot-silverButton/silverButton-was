package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Request.ScheduleCreateRequestDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleCreateResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
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
            (@RequestParam int year, @RequestParam int month, @AuthenticationPrincipal PrincipalUser principalUser){
        ResponseDto<List<ScheduleResponseDto>> result=
                scheduleservice.getScheduleByDependentIdAndDate(principalUser.getUserId(), year, month);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 자신의 스케줄 스스로 추가
    @PostMapping("/create")
    private ResponseEntity<ResponseDto<ScheduleCreateResponseDto>> createSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto dto, @AuthenticationPrincipal PrincipalUser principalUser){
        ResponseDto<ScheduleCreateResponseDto> result= scheduleservice.createScheduleSelf(dto, principalUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 자신이 요양사인 경우 피부양자의 일정에 추가가능한 기능
    // 프론트에서 버튼 클릭으로 자신의 일정에 추가할 것인지 피부양자의 일정에 추가할 것인지 선택할 수 있는 기능 추가할 것
    @PostMapping("/dependent-create")
    private ResponseEntity<ResponseDto<ScheduleCreateResponseDto>> createdependentSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto dto, @AuthenticationPrincipal PrincipalUser principalUser){

        ResponseDto<ScheduleCreateResponseDto> result= scheduleservice.createScheduleDependent(dto, principalUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 자신의 스케줄 수정(업데이트)
    @PutMapping("/update/{id}")
    private ResponseEntity<ResponseDto<ScheduleCreateResponseDto>> updateSchedule(
            @PathVariable Long id, @Valid @RequestBody ScheduleCreateRequestDto dto, @AuthenticationPrincipal PrincipalUser principalUser
    ){
        ResponseDto<ScheduleCreateResponseDto> result= scheduleservice.updateSchedule(id, dto, principalUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 선택된 본인의 스케줄 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteSchedule(@PathVariable Long id, @AuthenticationPrincipal PrincipalUser principalUser){
        ResponseDto<Void> result = scheduleservice.deleteSchedule(id, principalUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 오늘 스케줄 조회
    @GetMapping("/today")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getTodaySchedules(@AuthenticationPrincipal PrincipalUser principalUser) {
        ResponseDto<List<ScheduleResponseDto>> result = scheduleservice.getScheduleForToday(principalUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
