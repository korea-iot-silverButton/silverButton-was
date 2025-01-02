package com.korit.silverbutton.dto.medicine.requset;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineSearchByNameRequestDto {
    private String medicineName;
}

