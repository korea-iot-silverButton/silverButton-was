package com.korit.silverbutton.dto;

import lombok.Data;

@Data
public class SnsLoginResponseDto {

    private String userId;   // 사용자 아이디
    private String email;     // 사용자 이메일
    private boolean isPasswordResetRequired; // 비밀번호 재설정 필요 여부
    private String message;  // 성공/실패 메시지

    // 생성자
    public SnsLoginResponseDto(String userId, String email, boolean isPasswordResetRequired, String message) {
        this.userId = userId;
        this.email = email;
        this.isPasswordResetRequired = isPasswordResetRequired;
        this.message = message;
    }

    // 팩토리 메서드
    public static SnsLoginResponseDto fromUser(String userId, String email, boolean isPasswordResetRequired, String message) {
        return new SnsLoginResponseDto(userId, email, isPasswordResetRequired, message);

    }


}
