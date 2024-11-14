package com.korit.silverbutton.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/*
 * 복합키 정의
 * : @Embeddable 클래스 정의
 * */

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchingsId implements Serializable {
    @Column
    private Long dependentId;

    @Column
    private Long caregiverId;
}
