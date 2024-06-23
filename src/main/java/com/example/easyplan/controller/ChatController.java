package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ChatMessageDTO;
import com.example.easyplan.domain.dto.gpt.ChatGPTRequest;
import com.example.easyplan.domain.dto.gpt.ChatGPTResponse;
import com.example.easyplan.domain.entity.chat.ChatMessage;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.*;
import com.example.easyplan.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatController {
//
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final RestTemplate template;

    @MessageMapping("/room/{roomId}/send")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDTO sendMessage(@DestinationVariable("roomId") String roomId, @Payload ChatMessageDTO messageDTO, @Header("Authorization") String token ) {
        log.info("Received message: {}", messageDTO);
        log.info("token: " + token.substring(7));
        Long userId = tokenProvider.getUserId(token.substring(7));
        log.info("userId: " + userId);

        try {
            ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                    .orElseThrow(() -> new RuntimeException("채팅룸 찾을 수 없음"));

            ChatMessage message = new ChatMessage();
            message.setContent(messageDTO.getContent());

            User sender = getUserEmailFromContext(userId);
            message.setSender(sender);
            message.setChatRoom(chatRoom);

            ChatMessage savedMessage = chatMessageRepository.save(message);
            log.info("메시지: {}", savedMessage);

            return convertToDTO(savedMessage);
        } catch (Exception e) {
            log.error("sending msg error", e);
            throw e;
        }
    }

    private User getUserEmailFromContext(Long userId) {
        User sender = userRepository.findById(userId).get();

        return sender;
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .type(message.getType())
                .content(message.getContent())
                .senderEmail(message.getSender().getEmail())
                .chatRoomId(message.getChatRoom().getId())
                .build();
    }

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @MessageMapping("/room/{roomId}/gpt")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDTO sendGPT(@DestinationVariable("roomId") String roomId, @Payload ChatMessageDTO messageDTO, @Header("Authorization") String token ) {
        log.info("Received message: {}", messageDTO);
        log.info("token: " + token.substring(7));
        Long userId = tokenProvider.getUserId(token.substring(7));
        log.info("userId: " + userId);

        try {
            ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                    .orElseThrow(() -> new RuntimeException("채팅룸 찾을 수 없음"));

            ChatGPTRequest request = new ChatGPTRequest(model, messageDTO.getContent());
            ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
            log.info("gpt: " + response.getChoices().get(0).getMessage().getContent());

            ChatMessage message = new ChatMessage();
            message.setType(0);
            message.setContent(response.getChoices().get(0).getMessage().getContent());

            User sender = getUserEmailFromContext(userId);
            message.setSender(sender);
            message.setChatRoom(chatRoom);

            ChatMessage savedMessage = chatMessageRepository.save(message);
            log.info("메시지: {}", savedMessage);

            return convertToDTO(savedMessage);
        } catch (Exception e) {
            log.error("sending msg error", e);
            throw e;
        }
    }
}
