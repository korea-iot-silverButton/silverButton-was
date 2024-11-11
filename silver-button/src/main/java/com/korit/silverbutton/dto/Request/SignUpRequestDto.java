package com.korit.silverbutton.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;

    private String gender;

    @NotBlank
    private String nickName;

    @NotBlank
    private String rrn;

    @NotBlank
    private Date dateBirth;

    @NotBlank
    private String profile;

    private String license;

    private String specialization;
}