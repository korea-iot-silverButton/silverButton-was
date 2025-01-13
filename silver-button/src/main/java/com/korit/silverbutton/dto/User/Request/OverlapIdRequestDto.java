package com.korit.silverbutton.dto.User.Request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OverlapIdRequestDto {

    // 아이디 중복 확인 dto
    private String userId;
}
