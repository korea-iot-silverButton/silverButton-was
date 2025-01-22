package com.korit.silverbutton.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    boolean existsByDependentIdAndCaregiverId(Long dependentId, Long caregiverId);
}
