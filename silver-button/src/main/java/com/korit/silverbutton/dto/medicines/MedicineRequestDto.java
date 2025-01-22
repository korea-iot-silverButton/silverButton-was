package com.korit.silverbutton.dto.medicines;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequestDto {

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
}
