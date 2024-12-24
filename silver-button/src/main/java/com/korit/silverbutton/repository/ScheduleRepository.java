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
            "select " +
            "scd.id as schedule_id," +
            "        scd.dependent_id as schedule_user_id," +
            "        scd.task," +
            "        scd.schedule_date as scddate," +
            "        u.user_id as schedule_username," +
            "        null as sub_username " +
            "from " +
            "Schedules scd " +
            "        left outer join Users u on u.id = scd.dependent_id " +
            "union all " +
            "select " +
            "scd.id as schedule_id," +
            "        scd.dependent_id as schedule_user_id," +
            "        scd.task," +
            "        scd.schedule_date as scddate," +
            "        uc.user_id as schedule_username," +
            "        ud.user_id as sub_username " +
            "from " +
            "Matchings mc " +
            "        left outer join Schedules scd on scd.dependent_id = mc.dependent_id " +
            "        left outer join Users uc on uc.id = mc.caregiver_id " +
            "        left outer join Users ud on ud.id = mc.dependent_id " +
            ") " +
            "select " +
            "DATE_FORMAT(scddate, '%Y-%m-%d %H:%i:%s') AS schedule_date, task, schedule_username as user_id " +
            "from " +
            "temp_table " +
            "where " +
            "schedule_username = :userId " +
            "AND YEAR(scddate) = :year " +
            "AND MONTH(scddate) = :month", nativeQuery = true)
    List<Object[]> findSchedulesByDependentIdAndDate(@Param("userId") String userId,
                                                     @Param("year") int year,
                                                     @Param("month") int month);

    @Query("SELECT m.id.dependentId FROM Matchings m WHERE m.id.caregiverId = :caregiverId ")
    Long findDependentIdsByCaregiverId(@Param("caregiverId") Long caregiverId);
}


