package com.korit.silverbutton.dto.User.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerProfileDto {
    private String name;
    private String nickname;
    private String phone;
    private String email;
}
