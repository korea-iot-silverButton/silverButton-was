package com.korit.silverbutton.dto.medicine.requset;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequestDto {

    @Column
    private String itemName;
    // 약품 이름

    @Column
    private String drugShape;
    // 약품 모양
    @Column
    private String colorClass1;
    // 약품 색깔(앞)

    @Column
    private String colorClass2;
    // 약품 색깔(뒤)

    @Column
    private String lineFront;
    // 약품 분할선 모양(앞)

    @Column
    private String lineBack;
    // 약품 분할선 모양(뒤)

}

