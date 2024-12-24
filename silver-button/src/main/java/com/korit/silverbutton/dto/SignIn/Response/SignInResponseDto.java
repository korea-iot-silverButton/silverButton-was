package com.korit.silverbutton.dto.SignIn.Response;

import com.korit.silverbutton.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDto {
    private User user;
    private String token;
    private int exprTime;

    // 로그인 요청 처리시 필요한 constructor임 삭제하지말아요
    public SignInResponseDto(User user) {

    }
}