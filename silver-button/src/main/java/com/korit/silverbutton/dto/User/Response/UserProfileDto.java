package com.korit.silverbutton.dto.User.Response;

import com.korit.silverbutton.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String userId;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String nickname;

    private  Long protectorId; // 노인의 경우 요양사 아이디



    public UserProfileDto(User user){

        this.userId= user.getUserId();
        this.password= user.getPassword();
        this.name= user.getName();
        this.phone= user.getPhone();
        this.email= user.getEmail();
        this.nickname= user.getNickname();
        this.protectorId= user.getProtectorId();
    }
}
