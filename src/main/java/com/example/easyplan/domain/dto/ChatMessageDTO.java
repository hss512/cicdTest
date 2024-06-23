package com.example.easyplan.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private int type;
    private String content;
    private String senderEmail;
    private Long chatRoomId;
}
