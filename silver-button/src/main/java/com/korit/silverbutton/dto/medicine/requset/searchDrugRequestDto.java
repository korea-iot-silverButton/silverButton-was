package com.korit.silverbutton.dto.medicine.requset;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class searchDrugRequestDto {


    private String shape;


    private String color;


    private String line;


    private String name;


}

