package com.korit.silverbutton.dto.medicine;


import com.korit.silverbutton.entity.MedicineSchedule;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineScheduleResponseDto {
    private String itemSeq;

    private String itemName;

    private String useMethodQesitm;

    private String atpnQesitm;

    private String seQesitm;

    private String depositMethodQesitm;

    private String intrcQesitm;

    public MedicineScheduleResponseDto(MedicineSchedule medicineSchedule) {
        this.itemSeq = medicineSchedule.getItemSeq();
        this.itemName = medicineSchedule.getItemName();
        this.useMethodQesitm = medicineSchedule.getUseMethodQesitm();
        this.atpnQesitm = medicineSchedule.getAtpnQesitm();
        this.seQesitm = medicineSchedule.getSeQesitm();
        this.depositMethodQesitm = medicineSchedule.getDepositMethodQesitm();
        this.intrcQesitm = medicineSchedule.getIntrcQesitm();
    }


}
