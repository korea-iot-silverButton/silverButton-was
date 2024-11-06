package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
