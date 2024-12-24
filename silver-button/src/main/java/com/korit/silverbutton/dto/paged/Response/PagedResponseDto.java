package com.korit.silverbutton.dto.paged.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PagedResponseDto<T> {
    private T content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
