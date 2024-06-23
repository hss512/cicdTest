package com.example.easyplan.domain.dto;

import lombok.Data;

@Data
public class KakaoMessage {
    private String objType;
    private String text;
    private String btnTitle;
}
