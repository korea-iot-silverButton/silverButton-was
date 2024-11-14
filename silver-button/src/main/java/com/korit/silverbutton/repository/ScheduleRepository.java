package com.korit.silverbutton.repository;

import com.korit.silverbutton.dto.Schedule.ScheduleList;
import com.korit.silverbutton.entity.Matchings;
import com.korit.silverbutton.entity.MatchingsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Matchings, MatchingsId> {
    @Query("SELECT DATE_FORMAT(s.scheduleDate, '%Y-%m-%d %H:%i:%s') AS scheduleDate, s.task AS task " +
            "FROM Schedules s " +
            "LEFT JOIN Matchings m ON s.dependentId = m.id.dependentId OR s.dependentId = m.id.caregiverId " +
            "WHERE m.id.dependentId = :dependentId " +
            "AND YEAR(s.scheduleDate) = :year " +
            "AND MONTH(s.scheduleDate) = :month")
    List<Object[]> findSchedulesByDependentIdAndDate(@Param("dependentId") Long dependentId,
                                                    @Param("year") int year,
                                                    @Param("month") int month);
}
