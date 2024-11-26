package com.korit.silverbutton.service;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Request.ScheduleCreateRequestDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleCreateResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;

import com.korit.silverbutton.entity.MatchingsId;
import com.korit.silverbutton.entity.Schedules;
import com.korit.silverbutton.repository.ScheduleCreateRepository;
import com.korit.silverbutton.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleCreateRepository scheduleCreateRepository;

    // 주어진 아이디와 날짜로 해당 기간동안의 캘린더 호출
    public ResponseDto<List<ScheduleResponseDto>> getScheduleByDependentIdAndDate(String userId, int year, int month){
        List<ScheduleResponseDto> data= null;
        try{
            List<Object[]> results=
                    scheduleRepository.findSchedulesByDependentIdAndDate(userId, year, month);
            data= results.stream()
                    .map(result-> new ScheduleResponseDto((String)result[0], (String)result[1])).collect(Collectors.toList());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 자신이 피부양자일 경우 스케줄 스스로 추가
    public ResponseDto<ScheduleCreateResponseDto> createScheduleSelf(ScheduleCreateRequestDto dto){
        ScheduleCreateResponseDto data= null;
        try{
            Schedules schedule= new Schedules(null, dto.getDependentId(), dto.getScheduleDate(), dto.getTask());
            scheduleCreateRepository.save(schedule);
            data=new ScheduleCreateResponseDto(schedule);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 선택된 본인의 스케줄 삭제
    public ResponseDto<Void> deleteSchedule(Long id) {
        try{
            if(!scheduleCreateRepository.existsById(id)){
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
            scheduleCreateRepository.deleteById(id);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

}
