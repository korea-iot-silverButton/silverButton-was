package com.korit.silverbutton.dto.User.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequestDto {
    @NotEmpty(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotEmpty(message = "새로운 비밀번호는 필수입니다.")
    @Size(min = 8, message = "새로운 비밀번호는 최소 8자리 이상이어야 합니다.")
    private String newPassword;
}
