package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    List<Matching> findByUserId(String userId);
    List<Matching> findByDependent_Role(String role);
    List<Matching> findByCaregiver_Role(String role);

    List<Matching> findByCaregiverId(Long id);
    List<Matching> findByDependentId(Long id);

    List<Matching> findByDependent_IdAndCaregiver_Id(Long dependentId, Long caregiverId);
}