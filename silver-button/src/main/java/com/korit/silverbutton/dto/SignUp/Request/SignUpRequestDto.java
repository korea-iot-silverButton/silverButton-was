package com.korit.silverbutton.dto.SignUp.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Past
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false)
    private Date birthDate;

    @Column(columnDefinition = "ENUM('M', 'F')")
    private String gender;

    @Column(nullable = false)
    private String profileImage;

    @Column(columnDefinition = "ENUM('노인', '보호자', '요양사')")
    private String role;

    private String licenseNumber;

    private String specialization;

    private Long protectorId;
}