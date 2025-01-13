package com.korit.silverbutton.dto.User.Request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OverlapNicknameRequestDto {

    // 닉네임 중복 확인 dto
    private String nickname;
}
