package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;

public interface AuthService {

    ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseDto<SignInResponseDto> login(SignInRequestDto dto);

    ResponseDto<SignInResponseDto> dependentLogin(SignInRequestDto dto);
    ResponseDto<String> logout(String token);

    boolean overlapUserId(String userId);
    boolean overlapNickname(String nickName);


}