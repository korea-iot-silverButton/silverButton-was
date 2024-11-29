package com.korit.silverbutton.dto.SignUp.Response;

import com.korit.silverbutton.entity.User;
import lombok.Data;

@Data
public class SignUpResponseDto {
    User user;
    public SignUpResponseDto(User user) {
        this.user= user;
    }
}