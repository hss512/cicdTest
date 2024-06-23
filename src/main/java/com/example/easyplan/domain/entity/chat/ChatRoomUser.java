package com.example.easyplan.domain.entity.chat;

import com.example.easyplan.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoomUser_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id", nullable = false)
    @JsonIgnore
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnore
    private User user;

    @Builder
    public ChatRoomUser(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }
}
