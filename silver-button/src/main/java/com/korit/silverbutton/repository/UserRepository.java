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
    Optional<User> findByUserId(String userId);
    Optional<User> findByNameAndPhone(String name, String phone);

    boolean existsByPhone(String phone);
    boolean existsByNickname(String nickname);
    boolean existsByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.name = :name AND u.phone = :phone")
    User findByRoleAndNameAndPhone(@Param("role") String role, @Param("name") String name, @Param("phone") String phone);

    @Query("SELECT u FROM User u LEFT JOIN Matchings m ON u.id = m.id.caregiverId WHERE u.role = :role AND m.id.dependentId IS NULL")
    List<User> findNamesByRoleExcludeMatchingCaregiver(@Param("role") String role);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            "FROM Matchings m WHERE m.id.caregiverId = :id OR m.id.dependentId = :id")
    boolean existsByCaregiverOrDependentId(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.name = :name")
    Optional<User> findUserByEmailAndName(@Param("email") String email, @Param("name") String name);

}