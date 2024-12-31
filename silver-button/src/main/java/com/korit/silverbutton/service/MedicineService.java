package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.requset.MedicineSearchByNameRequestDto;
import com.korit.silverbutton.dto.medicine.response.MedicineResponseDto;

import java.util.List;

public interface MedicineService {
    ResponseDto<List<MedicineResponseDto>> searchMedicinesByName(
            MedicineSearchByNameRequestDto dto,
            int pageNo,
            int numOfRows
                                                                 );
}

