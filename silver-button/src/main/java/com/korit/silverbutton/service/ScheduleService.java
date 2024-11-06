package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Request.ScheduleRequestDto;
import com.korit.silverbutton.dto.Response.ResponseDto;
import com.korit.silverbutton.dto.Response.ScheduleResponseDto;
import com.korit.silverbutton.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    // 스케줄 생성
    public ResponseDto<ScheduleResponseDto> createSchedule(ScheduleRequestDto dto){
        try{
            return null;
        }
        catch(Exception e){
            return ResponseDto.setFailed("일정 등록 중 오류 발생"+ e.getMessage());
        }
    }

    public ResponseDto<ScheduleResponseDto> updateSchedule(Long id, ScheduleRequestDto dto){
        try{
            return null;
        }
        catch(Exception e){
            return ResponseDto.setFailed("업데이트 중 오류 발생"+ e.getMessage());
        }
    }
}
