package com.example.easyplan.domain.entity.chat;

import com.example.easyplan.domain.dto.ChatRoomDTO;
import com.example.easyplan.domain.entity.schedule.Reserve;
import com.example.easyplan.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    private String name;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;*/

    private Long ownerId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    @JsonIgnore
    private List<Reserve> reserves = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    public ChatRoom(String name, Long id, User user) {
        this.name = name;
        this.ownerId = id;
        this.user = user;
    }

    public void addUser(User user) {
        ChatRoomUser chatRoomUser = new ChatRoomUser(this, user);
        this.chatRoomUsers.add(chatRoomUser);
        user.getChatRoomUsers().add(chatRoomUser);
    }

    public void setOwner(Long id) {
        this.ownerId = id;
    }

    public ChatRoomDTO toDTO(){
        return ChatRoomDTO.builder()
                .id(this.id)
                .name(this.name)
                .ownerId(this.ownerId)
                .build();
    }
}

