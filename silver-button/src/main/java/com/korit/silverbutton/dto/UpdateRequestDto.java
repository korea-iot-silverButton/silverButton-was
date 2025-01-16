package com.korit.silverbutton.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateRequestDto {

    @NotBlank // 문자열이 null, 공백 "" 아닌지 검증하는 어노테이션
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String profileImg;
}