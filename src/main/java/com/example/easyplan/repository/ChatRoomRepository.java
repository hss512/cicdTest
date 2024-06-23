package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    void deleteById(Long id);
    List<ChatRoom> findAllByOwnerId(Long id);
}
