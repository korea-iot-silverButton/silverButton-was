package com.korit.silverbutton.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MatchingsId implements Serializable {
    @Column(name= "dependent_id")
    private Long dependentId;

    @Column(name= "caregiver_id")
    private Long caregiverId;
}
