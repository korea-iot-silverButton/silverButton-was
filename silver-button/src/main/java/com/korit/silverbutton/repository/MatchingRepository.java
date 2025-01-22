package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Matchings;
import com.korit.silverbutton.entity.MatchingsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matchings, MatchingsId> {
    boolean existsById_DependentIdAndId_CaregiverId(Long dependentId, Long caregiverId);
    Optional<Matchings> findById(MatchingsId id);
    void deleteById(MatchingsId id);
}
