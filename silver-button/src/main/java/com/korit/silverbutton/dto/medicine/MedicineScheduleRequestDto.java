package com.korit.silverbutton.dto.medicine;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineScheduleRequestDto {

    private Long itemSeq;

    private String itemName;

    private String useMethodQesitm;

    private String atpnQesitm;

    private String seQesitm;

    private String depositMethodQesitm;

    private String intrcQesitm;
}
