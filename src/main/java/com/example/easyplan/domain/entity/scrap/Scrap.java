package com.example.easyplan.domain.entity.scrap;

import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Scrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="scrap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}