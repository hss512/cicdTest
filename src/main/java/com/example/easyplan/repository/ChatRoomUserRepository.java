package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.chat.ChatRoomUser;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByChatRoom(ChatRoom chatRoom);
    void deleteByChatRoom(ChatRoom chatRoom);
}
