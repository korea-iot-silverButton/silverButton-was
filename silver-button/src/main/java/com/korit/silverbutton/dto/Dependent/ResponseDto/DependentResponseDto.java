package com.korit.silverbutton.dto.Dependent.ResponseDto;

import com.korit.silverbutton.entity.User;
import lombok.Getter;

@Getter
public class DependentResponseDto {

    private Long id;
    private String userId;
    private String name;
    private String phone;

    public DependentResponseDto(User user) {
        this.name = user.getName();
        this.phone = user.getPhone();
        this.userId = user.getUserId();
        this.id = user.getId();
    }

}