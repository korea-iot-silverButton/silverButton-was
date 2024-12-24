package com.korit.silverbutton.dto.User.Response;

import com.korit.silverbutton.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;

    private String userId;

    private String password;

    private String email;



    public UserResponseDto(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }


}
