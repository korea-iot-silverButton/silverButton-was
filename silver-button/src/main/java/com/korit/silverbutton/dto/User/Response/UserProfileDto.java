package com.korit.silverbutton.dto.User.Response;

import com.korit.silverbutton.entity.User;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private final String userId;
    private final String name;
    private final String phone;
    private final String email;
    private final String nickname;
    private final Long protectorId; // 노인의 경우 요양사 아이디

    public UserProfileDto(User user){
        this.userId= user.getUserId();
        this.name= user.getName();
        this.phone= user.getPhone();
        this.email= user.getEmail();
        this.nickname= user.getNickname();
        this.protectorId= user.getProtectorId();
    }
}
