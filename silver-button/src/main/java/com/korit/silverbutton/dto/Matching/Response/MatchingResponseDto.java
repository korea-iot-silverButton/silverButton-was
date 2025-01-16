package com.korit.silverbutton.dto.Matching.Response;

import com.korit.silverbutton.entity.Matching;
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

    public MatchingResponseDto(Matching matching) {
        this.dependentId = matching.getDependent().getId();
        this.caregiverId = matching.getCaregiver().getId();
        this.dependentName = matching.getDependent().getName();
        this.caregiverName = matching.getCaregiver().getName();
        this.phone=matching.getCaregiver().getPhone();
        this.email=matching.getCaregiver().getEmail();
        this.birthdate=matching.getCaregiver().getBirthDate();
        this.gender=matching.getCaregiver().getGender();
        this.profileImage=matching.getCaregiver().getProfileImage();
    }

}