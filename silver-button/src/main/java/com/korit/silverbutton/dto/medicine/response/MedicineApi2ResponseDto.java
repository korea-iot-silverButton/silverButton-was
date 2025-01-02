package com.korit.silverbutton.dto.medicine.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineApi2ResponseDto extends MedicineResponseDto {

    String itemName;
    public MedicineApi2ResponseDto(String sourceApi, String medicineName) {
        super(sourceApi, medicineName);
    }
}
