package com.example.easyplan.repository.schedule;

import com.example.easyplan.domain.entity.schedule.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findAllByChatRoomId(Long id);
}
