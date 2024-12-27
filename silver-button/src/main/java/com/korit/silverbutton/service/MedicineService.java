package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.medicine.response.MedicineResponseDto;
import com.korit.silverbutton.dto.ResponseDto;

import java.util.List;

public interface MedicineService {

    ResponseDto<List<MedicineResponseDto>> getDrugInfoByName(String name);

    ResponseDto<List<MedicineResponseDto>> getDrugInfoBySearchOption(String shape, String color, String line);


}
