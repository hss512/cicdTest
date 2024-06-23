package com.example.easyplan.domain.entity.chat;

import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type = 1;

    @Column(nullable = false, length = 4000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    @JsonIgnore
    private ChatRoom chatRoom;

    public ChatMessage(String content, User sender, ChatRoom chatRoom) {
        this.content = content;
        this.sender = sender;
        this.chatRoom = chatRoom;
    }
}
