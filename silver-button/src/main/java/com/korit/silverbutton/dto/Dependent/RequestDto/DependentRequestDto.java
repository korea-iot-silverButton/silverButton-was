package com.korit.silverbutton.dto.Dependent.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DependentRequestDto {

    private Long id;
    private String name;
    private String userId;
    private String phone;
    private String password;
}