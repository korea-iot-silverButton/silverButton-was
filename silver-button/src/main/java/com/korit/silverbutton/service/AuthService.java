package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.SnsLoginResponseDto;
import com.korit.silverbutton.dto.User.Request.OverlapIdRequestDto;
import com.korit.silverbutton.dto.User.Request.OverlapNicknameRequestDto;
import com.korit.silverbutton.entity.User;

import java.util.Optional;

public interface AuthService {

    ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseDto<SignInResponseDto> login(SignInRequestDto dto);

    // 아이디 중복 확인 로직
    boolean overlapUserId(String userId);

    // 닉네임 중복 확인 로직
    boolean overlapNickname(String nickName);

    // 이메일 인증을 통한 아이디 찾기
    ResponseDto<SnsLoginResponseDto> findUserIdByEmail(String email);

    // 이메일 인증 후 아이디 찾기
    ResponseDto<SnsLoginResponseDto> verifyUserId(String email, String token);

    // 비밀번호 재설정 링크 발송
    ResponseDto<SnsLoginResponseDto> sendPasswordResetLink(String email);

    // 비밀번호 재설정 및 변경
    ResponseDto<SnsLoginResponseDto> resetPassword(String token, String newPassword);


    ResponseDto<SignInResponseDto> dependentLogin(SignInRequestDto dto);

    ResponseDto<String> logout(String token);
}