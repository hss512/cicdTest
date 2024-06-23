package com.example.easyplan.service;

import com.example.easyplan.domain.dto.ChatMessageDTO;
import com.example.easyplan.domain.entity.chat.ChatMessage;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.chat.ChatRoomUser;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.ChatMessageRepository;
import com.example.easyplan.repository.ChatRoomRepository;
import com.example.easyplan.repository.ChatRoomUserRepository;
import com.example.easyplan.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, UserRepository userRepository, ChatMessageRepository chatMessageRepository, ChatRoomUserRepository chatRoomUserRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomUserRepository = chatRoomUserRepository;
    }

    @Transactional
    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public ChatRoom findRoomById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    @Transactional
    public ChatRoom createRoom(String name, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        ChatRoom chatRoom = new ChatRoom(name, owner.getId(), owner);
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void inviteUserToChatRoom(Long roomId, String email) {
        ChatRoom chatRoom = findRoomById(roomId);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ChatRoomUser chatRoomUser = new ChatRoomUser(chatRoom, user);
        chatRoomUserRepository.save(chatRoomUser);
    }

    @Transactional
    public List<ChatRoom> findRoomsForSenderReceiver(String userEmail, Long userId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<ChatRoomUser> chatRoomUsers = user.getChatRoomUsers();
        Set<ChatRoom> chatRooms = chatRoomUsers.stream()
                .map(ChatRoomUser::getChatRoom)
                .collect(Collectors.toSet()); // 중복 제거

        for (ChatRoom chatRoom : chatRooms) {
            log.info("SERVICE_Chat room_참여자: " + chatRoom.getName());
        }

        List<ChatRoom> allByOwnerId = chatRoomRepository.findAllByOwnerId(userId);

        for (ChatRoom chatRoom : allByOwnerId) {
            log.info("SERVICE_Chat room_방주인: " + chatRoom.getName());
            chatRooms.add(chatRoom);
        }

        return new ArrayList<>(chatRooms);
    }

    @Transactional
    public ChatMessage sendMessage(Long roomId, ChatMessageDTO messageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        User sender = userRepository.findByEmail(messageDTO.getSenderEmail())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(messageDTO.getContent());
        chatMessage.setSender(sender);
        chatMessage.setChatRoom(chatRoom);

        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public List<ChatMessageDTO> findAllChatByRoomId(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoom(chatRoom);

        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessage createChat(Long roomId, String senderEmail, String content) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender email: " + senderEmail));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID: " + roomId));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setSender(sender);
        chatMessage.setChatRoom(chatRoom);

        return chatMessageRepository.save(chatMessage);
    }

    private ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setType(chatMessage.getType());
        dto.setContent(chatMessage.getContent());
        dto.setSenderEmail(chatMessage.getSender().getEmail());
        dto.setChatRoomId(chatMessage.getChatRoom().getId());
        return dto;
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        ChatRoom chatRoom=chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new IllegalArgumentException("Room not found"));

        //채팅 메시지 삭제
        chatMessageRepository.deleteAll(chatRoom.getChatMessages());

        //채팅 사용자 관계 삭제
        chatRoomUserRepository.deleteAll(chatRoom.getChatRoomUsers());

        //룸 삭제
        chatRoomRepository.deleteById(roomId);

    }
}
