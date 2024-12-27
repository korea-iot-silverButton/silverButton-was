package com.korit.silverbutton.dto.medicine.response;

import com.korit.silverbutton.entity.MedicineSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDto {

    private Long id;
    // 약품 고유 식별자

    private String itemSeq;
    // 약품 아이디

    private String itemName;
    // 약품 이름

    private String entpName;
    // 제조사 이름

    private String efcyQesitm;
    // 약품 사용(복용)방법

    private String useMethodQesitm;
    // 약품 부작용

    private String atpnQesitm;
    // 약품 복용시 주의사항

    private String seQesitm;
    // 약품 보관 방법

    private String depositMethodQesitm;
    // 약품 효능

    private String intrcQesitm;
    // 약품 표시(앞)

    private String itemImage;
    // 약품 표시(뒤)

    @Column
    private String drugShape;
    // 약품 모양

    @Column(name = "color_class1")
    private String colorClass1;
    // 약품 색깔(앞)

    private String colorClass2;
    // 약품 색깔(뒤)

    @Column(name = "line_front")
    private String lineFront;
    // 약품 분할선 모양(앞)

    private String lineBack;

    public MedicineResponseDto(MedicineSchedule medicine) {
        this.id = medicine.getId();
        this.itemSeq = medicine.getItemSeq();
//        this.ItemName = medicine.getItemName();
//        this.McName = medicine.getMcName();
//        this.Efcyqesitm = medicine.getEfcyqesitm();
//        this.Usemethodqesitm = medicine.getUsemethodqesitm();
//        this.Atpnqesitm = medicine.getAtpnqesitm();
//        this.Seqesitm = medicine.getSeqesitm();
//        this.Depositmethodqesitm = medicine.getDepositmethodqesitm();
//        this.Intrcqesitm = medicine.getIntrcqesitm();
//        this.ItemImage = medicine.getItemImage();
//        this.DrugShape = medicine.getDrugShape();
//        this.ColorClass1 = medicine.getColorClass1();
//        this.ColorClass2 = medicine.getColorClass2();
//        this.LineFront = medicine.getLineFront();
//        this.LineBack = medicine.getLineBack();


    }


    // 약품 분할선 모양(뒤)


}