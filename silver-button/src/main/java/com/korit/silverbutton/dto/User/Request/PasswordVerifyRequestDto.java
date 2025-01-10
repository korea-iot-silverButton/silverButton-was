package com.korit.silverbutton.dto.User.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordVerifyRequestDto {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;
}