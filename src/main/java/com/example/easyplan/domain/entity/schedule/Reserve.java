package com.example.easyplan.domain.entity.schedule;

import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.chat.ChatRoom;
import com.example.easyplan.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reserve extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type = 0;

    @Column(length = 4000)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatRoom_id")
    @JsonIgnore
    private ChatRoom chatRoom;

    @Builder
    public Reserve(String content, ChatRoom chatRoom){
        this.content = content;
        this.chatRoom = chatRoom;
    }
}
