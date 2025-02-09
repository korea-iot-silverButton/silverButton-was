package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.HealthMagazine.response.HealthMagazineResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleResponseDto;
import com.korit.silverbutton.dto.medicines.MedicineRequestDto;
import com.korit.silverbutton.dto.medicines.MedicineResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicineService {

    ResponseDto<MedicineResponseDto> postMedicine(MedicineRequestDto dto);

    ResponseDto<List<MedicineResponseDto>> findAll(MedicineRequestDto dto);

    ResponseDto<List<MedicineResponseDto>> getMedicineByItemName(String itemName);

    ResponseDto<MedicineResponseDto> getMedicineById(Long id);


}
