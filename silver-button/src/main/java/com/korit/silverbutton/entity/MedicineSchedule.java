package com.korit.silverbutton.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    @Column(name = "id")
    private Long id;

    // 약품 아이디
    @Column(name = "user_id")
    private String userId;

    // 약품 고유 번호
    @Column(name = "item_seq")
    private Long itemSeq;

    /// 약품 이름
    @Column(name = "item_name")
    private String itemName;

    /// 약품 사용방법
    @Column(name = "use_method_qesitm")
    private String useMethodQesitm;

    /// 약품 복용 시 주의사항
    @Column(name = "atpn_qesitm")
    private String atpnQesitm;

    /// 약품 부작용
    @Column(name = "se_qesitm")
    private String seQesitm;

    // 약품 보관 방법
    @Column(name = "deposit_method_qesitm")
    private String depositMethodQesitm;

    // 약품  상호작용
    @Column(name = "intrc_qesitm")
    private String intrcQesitm;

    // 약품 이미지
    @Column(name = "medicine_image")
    private String medicineImage;

}