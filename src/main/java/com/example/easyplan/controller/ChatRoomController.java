package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ChatMessageDTO;
import com.example.easyplan.domain.dto.ChatRoomDTO;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatRoom")
@Log4j2
public class ChatRoomController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    @GetMapping("/sender/info")
    public ResponseEntity<?> getUserEmailFromContext() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findById(Long.parseLong(userId)).get();
        return ResponseEntity.ok(sender.getEmail());
    }

    @GetMapping("/roomList")
    public ResponseEntity<?> roomList() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String userEmail = userRepository.findById(Long.parseLong(userId)).get().getEmail();
        List<ChatRoom> roomList = chatService.findRoomsForSenderReceiver(userEmail, Long.parseLong(userId));
        for (ChatRoom chatRoom : roomList) {
            log.info("chat_roo_name: " + chatRoom.getName());
        }
        List<ChatRoomDTO> list = roomList.stream().map(ChatRoom::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/createRoom")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestParam("name") String name, @RequestBody List<String> inviteEmails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return userRepository.findById(Long.parseLong(userId)).map(user -> {
            String ownerEmail = user.getEmail();
            log.info("발신자 이메일: " + ownerEmail);
            ChatRoom chatRoom = chatService.createRoom(name, ownerEmail);

            for (String inviteEmail : inviteEmails) {
                if (inviteEmail == null || inviteEmail.isEmpty()) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "이메일을 입력하세요.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }

                Optional<User> invitee = userRepository.findByEmail(inviteEmail);
                if (!invitee.isPresent()) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "존재하지 않는 이메일입니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }

                chatService.inviteUserToChatRoom(chatRoom.getId(), inviteEmail);
            }

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("roomId", chatRoom.getId());
            return ResponseEntity.ok(successResponse);
        }).orElseGet(() -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "사용자 찾을 수 없음");
            return ResponseEntity.badRequest().body(errorResponse);
        });
    }


    @PostMapping("/{roomId}")
    public ResponseEntity<Void> sendMessage(@PathVariable("roomId") Long roomId, @RequestBody ChatMessageDTO messageDTO) {
        chatService.sendMessage(roomId, messageDTO);
        log.info("send 성공");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable("roomId") Long roomId) {
        List<ChatMessageDTO> messages = chatService.findAllChatByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable("roomId") Long roomId){
        try{
            chatService.deleteRoom(roomId);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "채팅방 삭제 성공");
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
