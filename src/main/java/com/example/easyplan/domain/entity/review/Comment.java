package com.example.easyplan.domain.entity.review;

import com.example.easyplan.domain.dto.ResponseCommentDTO;
import com.example.easyplan.domain.entity.BaseEntity;
import com.example.easyplan.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name= "review_id")
    private Review review;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Review review, User user, String content){
        this.review = review;
        this.user = user;
        this.content = content;
    }

    public static ResponseCommentDTO toDTO(Comment comment){
        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .email(comment.getUser().getEmail())
                .content(comment.getContent())
                .build();
    }
}
