package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Matchings;
import com.korit.silverbutton.entity.MatchingsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Matchings, MatchingsId> {

    // 날짜로 조회해서 띄우기
    @Query(value = "with temp_table as( " +
            "SELECT " +
            "scd.id AS schedule_id, " +
            "scd.dependent_id AS schedule_user_id, " +
            "scd.task, " +
            "scd.schedule_date AS scddate, " +
            "u.user_id AS schedule_username " +
            "FROM Schedules scd " +
            "LEFT OUTER JOIN Users u ON u.id = scd.dependent_id " +
            "UNION ALL " +
            "SELECT " +
            "scd.id AS schedule_id, " +
            "scd.dependent_id AS schedule_user_id, " +
            "scd.task, " +
            "scd.schedule_date AS scddate, " +
            "uc.user_id AS schedule_username " +
            "FROM Matchings mc " +
            "LEFT OUTER JOIN Schedules scd ON scd.dependent_id = mc.dependent_id " +
            "LEFT OUTER JOIN Users uc ON uc.id = mc.caregiver_id " +
            "LEFT OUTER JOIN Users ud ON ud.id = mc.dependent_id " +
            ") " +
            "SELECT " +
            "schedule_id, " +
            "task, " +
            "DATE_FORMAT(scddate, '%Y-%m-%d %H:%i:%s') AS schedule_date, " +
            "schedule_username AS user_id " +
            "FROM temp_table " +
            "WHERE schedule_username = :userId " +
            "AND YEAR(scddate) = :year " +
            "AND MONTH(scddate) = :month", nativeQuery = true)
    List<Object[]> findSchedulesByDependentIdAndDate(@Param("userId") String userId,
                                                     @Param("year") int year,
                                                     @Param("month") int month);

    @Query("SELECT m.id.dependentId FROM Matchings m WHERE m.id.caregiverId = :caregiverId ")
    Long findDependentIdsByCaregiverId(@Param("caregiverId") Long caregiverId);
}


