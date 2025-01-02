package com.korit.silverbutton.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchingsId implements Serializable {
    @Column(name = "dependent_id") // 컬럼 이름 지정
    private Long dependentId;

    @Column(name = "caregiver_id") // 컬럼 이름 지정
    private Long caregiverId;
}
