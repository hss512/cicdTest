package com.example.easyplan.repository.schedule;

import com.example.easyplan.domain.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllBySendMailTime(String sendEmailTime);

    void deleteByReserveId(Long reserveId);

    @Modifying
    @Query("delete Schedule s WHERE s.id IN :ids")
    void deleteSchedulesByIds(List<Long> ids);
}
