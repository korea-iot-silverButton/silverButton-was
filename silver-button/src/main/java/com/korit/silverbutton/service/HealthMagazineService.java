package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.HealthMagazine.request.HealthMagazineRequestDto;
import com.korit.silverbutton.dto.HealthMagazine.response.HealthMagazineResponseDto;
import com.korit.silverbutton.dto.ResponseDto;

import java.util.List;

public interface HealthMagazineService {
    ResponseDto<HealthMagazineResponseDto> postHealthMagazine(HealthMagazineRequestDto dto);

    ResponseDto<List<HealthMagazineResponseDto>> findAll(HealthMagazineRequestDto dto);

    ResponseDto<HealthMagazineResponseDto> getHealthMagazineById(Long id);

    ResponseDto<Boolean> deleteHealthMagazineById(Long id);

    ResponseDto<List<HealthMagazineResponseDto>> findTop5ByOrderByViewCountDesc();

    ResponseDto<List<HealthMagazineResponseDto>> findLatestByOrderByPublishedDateDesc();

    ResponseDto<List<HealthMagazineResponseDto>> findAllByOrderByViewCountDesc();

}
