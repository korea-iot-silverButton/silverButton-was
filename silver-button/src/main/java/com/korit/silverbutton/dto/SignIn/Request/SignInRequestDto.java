package com.korit.silverbutton.dto.SignIn.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignInRequestDto {

    private String userId;

    @NotBlank
    private String password;

    private String phone;

    private String name;

    private String role;


}