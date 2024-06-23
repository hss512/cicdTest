package com.example.easyplan.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCommentDTO {
    private Long id;
    private String email;
    private String content;
    private int check;
}
