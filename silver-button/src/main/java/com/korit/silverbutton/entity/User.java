package com.korit.silverbutton.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Builder(toBuilder = true) // 클래스 수준에 추가되는 어노테이션. 객체 생성 시 빌더 패턴 사용 가능하게 도와줌
//(toBuilder = true) 옵션 : 이미 생성된 객체에서 빌더를 사용할 수 있도록 설정
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Past
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false)
    private Date birthDate;

    @Column(columnDefinition = "ENUM('M', 'F')")
    private String gender;

    @Column(nullable = false)
    private String profileImage;

    @Column(columnDefinition = "ENUM('노인', '보호자', '요양사')")
    private String role;

    private String licenseNumber;

    private String specialization;

    private Long protectorId;
}