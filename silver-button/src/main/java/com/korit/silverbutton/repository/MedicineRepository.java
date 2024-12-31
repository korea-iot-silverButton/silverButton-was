package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineSchedule, Long> {
//    List<Medicine> getDrugInfoBySearchOption(
//            String drugShape, String color, String line, String name);
//
//    List<Medicine> getDrugInfoByName(
//            String name );

}
