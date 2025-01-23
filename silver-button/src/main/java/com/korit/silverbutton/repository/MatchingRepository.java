package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Matchings;
import com.korit.silverbutton.entity.MatchingsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matchings, MatchingsId> {
    boolean existsById_DependentIdAndId_CaregiverId(Long dependentId, Long caregiverId);
    Optional<Matchings> findById(MatchingsId id);
    void deleteById(MatchingsId id);

    @Query("SELECT m.id FROM Matchings m WHERE m.id.caregiverId = :caregiverId")
    Optional<MatchingsId> findIdByCaregiverId(@Param("caregiverId") Long caregiverId);

    @Query("SELECT m.id FROM Matchings m WHERE m.id.dependentId = :dependentId")
    Optional<MatchingsId> findIdByDependentId(@Param("dependentId") Long dependentId);
}
