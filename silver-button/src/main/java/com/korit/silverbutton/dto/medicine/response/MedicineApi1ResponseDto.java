package com.korit.silverbutton.dto.medicine.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineApi1ResponseDto extends MedicineResponseDto {

    String ITEM_NAME;
    public MedicineApi1ResponseDto(String sourceApi, String medicineName) {
        super(sourceApi, medicineName);
    }
}
