package com.example.easyplan.domain.entity.schedule;

import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reserveId;

    private String content;

    private String sendMailTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
