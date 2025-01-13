package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNameAndPhone(String name, String phone);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.name = :name AND u.phone = :phone")
    User findByRoleAndNameAndPhone(@Param("role") String role, @Param("name") String name, @Param("phone") String phone);


    boolean existsByUserId(String userId);
}