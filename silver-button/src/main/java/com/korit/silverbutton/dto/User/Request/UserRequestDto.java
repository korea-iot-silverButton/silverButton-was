package com.korit.silverbutton.dto.User.Request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private Long id;
    private String userId;
    private String name;
    private String phone;
    private String email;
    private String nickname;
    private String password;
}
