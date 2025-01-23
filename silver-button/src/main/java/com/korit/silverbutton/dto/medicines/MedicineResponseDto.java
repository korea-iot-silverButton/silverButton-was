package com.korit.silverbutton.dto.medicines;

import com.korit.silverbutton.entity.Medicine;
import com.korit.silverbutton.entity.MedicineSchedule;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDto {

    private Long id;

    private Long itemSeq;

    private String itemName;

    private String efcyQesitm;

    private String useMethodQesitm;

    private String atpnQesitm;

    private String seQesitm;

    private String depositMethodQesitm;

    private String intrcQesitm;

    private String medicineImage;

    public MedicineResponseDto(Medicine medicine) {
        this.id = medicine.getId();
        this.itemSeq = medicine.getItemSeq();
        this.itemName = medicine.getItemName();
        this.useMethodQesitm = medicine.getUseMethodQesitm();
        this.atpnQesitm = medicine.getAtpnQesitm();
        this.seQesitm = medicine.getSeQesitm();
        this.depositMethodQesitm = medicine.getDepositMethodQesitm();
        this.intrcQesitm = medicine.getIntrcQesitm();
        this.medicineImage = medicine.getMedicineImage();
        this.efcyQesitm = medicine.getEfcyQesitm();
    }


}
