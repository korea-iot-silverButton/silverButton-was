package com.korit.silverbutton.dto.Response;

import com.korit.silverbutton.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpResponseDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String nickname;
    private String profile;
    private String phone;
    private String gender;
    private String rrn;
    private Date dateBirth;
    private String license;
    private String specialization;

    public SignUpResponseDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname=user.getNickname();
        this.profile=user.getProfile();
    }
}