package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.KakaoMessage;
import com.example.easyplan.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/kakao/message")
    public ResponseEntity<?> kakaoMessage(KakaoMessage kakaoMessage){

        boolean result = messageService.sendMessage("", kakaoMessage);

        log.info("result= " + result);

        return ResponseEntity.ok("메시지 전송 성공");
    }
}
