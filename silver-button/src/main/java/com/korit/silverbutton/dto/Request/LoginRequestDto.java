package com.korit.silverbutton.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDto {

    private String userId;

    @NotBlank
    private String password;

    private String phone;
}