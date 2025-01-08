package com.korit.silverbutton.service;


import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicineScheduleService {
    ResponseDto<MedicineScheduleResponseDto> postMedicineByUserId(String userId, MedicineScheduleRequestDto dto);

    ResponseDto<List<MedicineScheduleResponseDto>> getMedicineAllByUserId(String userId);

    ResponseDto<MedicineScheduleResponseDto> getMedicineByUserIdAndItemSeq(String userId, Long itemSeq);

    ResponseDto<Boolean> deleteMedicineByUserIdAndItemSeq(String userId, Long itemSeq);
}
