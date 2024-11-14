package com.korit.silverbutton.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "schedules")
@NoArgsConstructor
public class Schedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dependentId;

    @Column(nullable = false)
    private Date scheduleDate;

    @Column(nullable = false)
    private String task;

}
