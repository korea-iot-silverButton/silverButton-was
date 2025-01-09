package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.HealthMagazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthMagazineRepository extends JpaRepository<HealthMagazine, Long> {

    Optional<HealthMagazine> getHealthMagazineById(Long id);

    void deleteHealthMagazineById(Long id);

    @Query(value = "SELECT * FROM health_magazine ORDER BY view_count DESC LIMIT 5", nativeQuery = true)
    List<HealthMagazine> findTop5ByOrderByViewCountDesc();
}
