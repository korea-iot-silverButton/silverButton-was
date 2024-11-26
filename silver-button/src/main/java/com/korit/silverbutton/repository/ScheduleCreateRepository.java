package com.korit.silverbutton.repository;

import com.korit.silverbutton.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleCreateRepository extends JpaRepository<Schedules, Long> {
    // 자신이 피부양자일 경우 스케줄 스스로 추가
    // service, controller로 작성하였음

    // 자신이 부양자, 요양사일 경우 matchings 테이블 조회 후 추가

    // 삭제 시 본인인지 userId확인하고 삭제

}
