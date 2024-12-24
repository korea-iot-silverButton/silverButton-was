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

    // 외부에서 접근 가능하도록 constructor 만듬
    // DependentServiceImpl 에서 값 받아오기 위해 만들엇음 삭제 ㄴㄴ
    public SignInResponseDto(User user) {

    }
}