package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.Request.LoginRequestDto;
import com.korit.silverbutton.dto.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.Response.LoginResponseDto;
import com.korit.silverbutton.dto.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;

public interface AuthService {

    ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseDto<LoginResponseDto> login(LoginRequestDto dto);
}