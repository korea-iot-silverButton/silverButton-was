package com.korit.silverbutton.service;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.Schedule.Request.ScheduleCreateRequestDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleCreateResponseDto;
import com.korit.silverbutton.dto.Schedule.Response.ScheduleResponseDto;

import com.korit.silverbutton.entity.Schedules;
import com.korit.silverbutton.repository.ScheduleCreateRepository;
import com.korit.silverbutton.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
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
                    .map(result-> new ScheduleResponseDto((Long)result[0], (String)result[2], (String)result[1])).collect(Collectors.toList());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 자신이 피부양자일 경우 스케줄 스스로 추가
    public ResponseDto<ScheduleCreateResponseDto> createScheduleSelf(ScheduleCreateRequestDto dto, Long userId){
        ScheduleCreateResponseDto data= null;
        try{
            Schedules schedule= new Schedules(null, userId, dto.getScheduleDate(), dto.getTask());
            scheduleCreateRepository.save(schedule);
            data=new ScheduleCreateResponseDto(schedule);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 자신이 요양사인 경우 피부양자의 일정에 추가가능한 기능
    public ResponseDto<ScheduleCreateResponseDto> createScheduleDependent(ScheduleCreateRequestDto dto, Long userId) {// users의 pk인 id== 여기서의 userId 헷갈림 조심
        try{
            // 매칭 테이블에서 userId로 들어온 id값을 매칭에서 검색 후 있으면 피부양자 id반환, 없을 경우 현재 매칭된 피부양자 없음 메시지 반환
            Long dependentId= scheduleRepository.findDependentIdsByCaregiverId(userId);
            if(dependentId!= null){
                return createScheduleSelf(dto, dependentId);
            }
            else{
                return ResponseDto.setFailed("피부양자가 없습니다.");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 선택된 본인의 스케줄 삭제
    @Transactional
    public ResponseDto<Void> deleteSchedule(Long id, Long userId) { // users의 pk인 id== 여기서의 userId 헷갈림 조심
        try{
            if(!scheduleCreateRepository.existsById(id)){
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_POST);
            }
            scheduleCreateRepository.deleteByIdAndDependentId(id, userId);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 본인의 스케줄 업데이트
    public ResponseDto<ScheduleCreateResponseDto> updateSchedule(Long id, ScheduleCreateRequestDto dto, Long userId) {
        try{
            boolean check= scheduleCreateRepository.existsByIdAndDependentId(id, userId);
            if (check) {
                Schedules schedule = scheduleCreateRepository.findByIdAndDependentId(id, userId);

                // 스케줄이 존재하면 수정
                schedule.setTask(dto.getTask());

                // 스케줄 저장 (업데이트)
                scheduleCreateRepository.save(schedule);

                // 응답 데이터 반환
                ScheduleCreateResponseDto responseDto = new ScheduleCreateResponseDto(schedule);
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
            } else {
                return ResponseDto.setFailed("해당 스케줄이 존재하지 않습니다. 다시 시도해주세요");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
}
