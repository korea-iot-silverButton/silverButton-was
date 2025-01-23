package com.korit.silverbutton.repository;

import com.korit.silverbutton.dto.medicine.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicines.MedicineRequestDto;
import com.korit.silverbutton.entity.HealthMagazine;
import com.korit.silverbutton.entity.Medicine;
import com.korit.silverbutton.entity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {

    @Query("SELECT m FROM Medicine m WHERE m.itemName LIKE %:itemName%")
    Optional<List<Medicine>> getMedicineByItemName(@Param("itemName")String itemName);

    Optional<Medicine> getMedicineById(Long id);

}
