package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.MedicineSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {

    Optional<List<MedicineSchedule>> getMedicineAllByUserId(String userId);

    Optional<MedicineSchedule> getMedicineByUserIdAndItemSeq(String userId, Long itemSeq);

    void deleteMedicineByUserIdAndItemSeq(String userId, Long itemSeq);
}
