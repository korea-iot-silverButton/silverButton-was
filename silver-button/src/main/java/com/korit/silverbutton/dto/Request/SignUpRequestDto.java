package com.korit.silverbutton.dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SignUpRequestDto {
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String confirmPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private Date birthDate;

    @Column(columnDefinition = "ENUM('M', 'F')")
    private String gender;

    @Column(nullable = false)
    private String profileImage;

    @Column(columnDefinition = "ENUM('노인', '보호자', '요양사')")
    private String role;

    private String licenseNumber;

    private String specialization;

    private String protectorId;
}