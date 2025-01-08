package com.korit.silverbutton.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "medicine_schedule")
@Builder
public class MedicineSchedule {

    // 각 약품 스케줄에 대한 고유값

    // 사용자 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 약품 아이디
    @Column(nullable = false)
    private String userId;

    // 약품 고유 번호
    @Column
    private String itemSeq;

    /// 약품 이름
    @Column
    private String itemName;

    /// 약품 사용방법
    @Column
    private String useMethodQesitm;

    /// 약품 복용 시 주의사항
    @Column
    private String atpnQesitm;

    /// 약품 부작용
    @Column
    private String seQesitm;

    // 약품 보관 방법
    @Column
    private String depositMethodQesitm;

    // 약품  상호작용
    @Column
    private String intrcQesitm;


}