package com.korit.silverbutton.dto.Matching.Response;

import com.korit.silverbutton.entity.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingResponseDto {
    private Long dependentId;
    private Long caregiverId;
    private String dependentName;
    private String caregiverName;
    private String phone;
    private String email;
    private Date birthdate;
    private String gender;
    private String profileImage;

}