package com.example.easyplan.service;

import com.example.easyplan.domain.dto.KakaoMessage;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class MessageService {

    private static final String URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    public boolean sendMessage(String token, KakaoMessage kakaoMessage){
        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", kakaoMessage.getObjType());
        templateObj.put("content", kakaoMessage.getText());
        templateObj.put("button_title", kakaoMessage.getBtnTitle());

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        header.set("Authorization", "Bearer " + token);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", templateObj.toString());

        HttpEntity<?> messageEntity = new HttpEntity<>(parameters, header);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, messageEntity, String.class);

        log.info(response.getBody());

        return true;
    }

}
