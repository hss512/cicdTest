package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.chat.ChatMessage;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySender(User sender);
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
    void deleteAllByChatRoom(ChatRoom chatRoom);
}
