package com.example.easyplan.domain.entity.review;

import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "review")
    @JsonManagedReference
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Photo> photos = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    private int viewCount;

    private Long heartCount=0L;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setReview(this);
    }

    public void increaseHeartCount(){
        this.heartCount++;
    }

    public void decreaseHeartCount(){
        this.heartCount--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return Objects.equals(getId(), review.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}