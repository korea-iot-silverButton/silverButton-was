package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // 아이디로 사용자 찾기

    Optional<User> findByEmail(String email);  // 이메일로 사용자 찾기

    Optional<User> findByNameAndEmail(String name, String email);  // 이름과 이메일로 사용자 찾기

    Optional<User> findByNameAndPhone(String name, String phone); // 이름이랑 전화번호로 노인 찾기

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.name = :name AND u.phone = :phone")
    User findByRoleAndNameAndPhone(@Param("role") String role, @Param("name") String name, @Param("phone") String phone);

    @Query("SELECT u FROM User u WHERE u.role= :role")
    List<User> findNamesByRole(@Param("role") String role);

    @Query("SELECT u FROM User u LEFT JOIN Matching m ON u.id = m.caregiver.id WHERE u.role = :role AND m.caregiver IS NULL")
    List<User> findNamesByRoleExcludeMatchingCaregiver(@Param("role") String role);
}