package com.korit.silverbutton.dto.medicine.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDto {
    private String sourceApi;  // api 구분값
    private String medicineName;  // 약품명(검색한 약품 이름)

}