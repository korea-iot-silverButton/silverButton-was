package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    @Query("SELECT m FROM Matching m WHERE m.caregiver.userId = :userId OR m.dependent.userId = :userId")
    List<Matching> findAllByCaregiverUserIdOrDependentUserId(@Param("userId") String userId);
}
