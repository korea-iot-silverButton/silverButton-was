package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.User.Request.OverlapIdRequestDto;
import com.korit.silverbutton.dto.User.Request.OverlapNicknameRequestDto;

public interface AuthService {

    ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseDto<SignInResponseDto> login(SignInRequestDto dto);

    // 아이디 중복 확인 로직
    boolean overlapUserId(String userId);

    // 닉네임 중복 확인 로직
    boolean overlapNickname(String nickName);

    ResponseDto<SignInResponseDto> dependentLogin(SignInRequestDto dto);

    ResponseDto<String> logout(String token);
}