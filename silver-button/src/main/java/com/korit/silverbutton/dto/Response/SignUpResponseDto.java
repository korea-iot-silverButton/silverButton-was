package com.korit.silverbutton.dto.Response;

import com.korit.silverbutton.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpResponseDto {
    User user;
    public SignUpResponseDto(User user) {
        this.user= user;
    }
}