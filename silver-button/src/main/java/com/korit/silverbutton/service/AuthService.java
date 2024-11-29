package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;

public interface AuthService {

    ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseDto<SignInResponseDto> login(SignInRequestDto dto);
}