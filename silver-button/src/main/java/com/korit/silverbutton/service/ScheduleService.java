package com.korit.silverbutton.service;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;
import com.korit.silverbutton.dto.Schedule.ScheduleList;
import com.korit.silverbutton.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ResponseDto<List<ScheduleResponseDto>> getScheduleByDependentIdAndDate(Long dependentId, int year, int month){
        List<ScheduleResponseDto> data= null;
        try{
            List<Object[]> results=
                    scheduleRepository.findSchedulesByDependentIdAndDate(dependentId, year, month);
            data= results.stream()
                    .map(result-> new ScheduleResponseDto((String)result[0], (String)result[1])).collect(Collectors.toList());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}
