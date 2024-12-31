package com.korit.silverbutton.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDrugApi<D> {
    private int pageNo;
    private int totalCount;
    private int numberOfRows;
    private D[] drugs;
}
