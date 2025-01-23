package com.korit.silverbutton.dto.medicine;

import com.korit.silverbutton.entity.MedicineSchedule;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineScheduleResponseDto {

    private Long id;

    private String userId;

    private Long itemSeq;

    private String itemName;

    private String efcyQesitm;

    private String useMethodQesitm;

    private String atpnQesitm;

    private String seQesitm;

    private String depositMethodQesitm;

    private String intrcQesitm;

    private String medicineImage;

    public MedicineScheduleResponseDto(MedicineSchedule medicineSchedule) {
        this.id = medicineSchedule.getId();
        this.userId = medicineSchedule.getUserId();
        this.itemSeq = medicineSchedule.getItemSeq();
        this.itemName = medicineSchedule.getItemName();
        this.efcyQesitm = medicineSchedule.getEfcyQesitm();
        this.useMethodQesitm = medicineSchedule.getUseMethodQesitm();
        this.atpnQesitm = medicineSchedule.getAtpnQesitm();
        this.seQesitm = medicineSchedule.getSeQesitm();
        this.depositMethodQesitm = medicineSchedule.getDepositMethodQesitm();
        this.intrcQesitm = medicineSchedule.getIntrcQesitm();
        this.medicineImage = medicineSchedule.getMedicineImage();
    }


}
