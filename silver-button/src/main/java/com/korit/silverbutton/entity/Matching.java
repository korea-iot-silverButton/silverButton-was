package com.korit.silverbutton.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matchings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id",referencedColumnName = "user_Id", nullable = false)
//    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", referencedColumnName = "id", nullable = false)
    private User dependent;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id", referencedColumnName = "id", nullable = false)
    private User caregiver;



}
