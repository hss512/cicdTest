package com.example.easyplan.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDTO {

    private Long id;

    private String name;

    private Long ownerId;
}
