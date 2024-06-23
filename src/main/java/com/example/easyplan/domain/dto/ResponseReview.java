package com.example.easyplan.domain.dto;

import com.example.easyplan.domain.entity.review.Comment;
import com.example.easyplan.domain.entity.review.Heart;
import com.example.easyplan.domain.entity.review.Photo;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponseReview {
    private Long id;
    private String title;
    private List<Photo> photos = new ArrayList<>();
    private String content;
    private Long userId;
    private List<Heart> heartList = new ArrayList<>();
    private int viewCount;
    private  Long heartCount;
    private List<ResponseCommentDTO> commentList = new ArrayList<>();

    public ResponseReview(){};

    public List<String> getPhotoUrls() {
        return this.photos.stream()
                .map(photo -> "http://localhost:8090/api/files/" + photo.getFileName())
                .collect(Collectors.toList());
    }


    public ResponseReview(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.photos = review.getPhotos();
        this.content = review.getContent();
        this.heartList = review.getHeartList();
        this.viewCount = review.getViewCount();
        this.heartCount = review.getHeartCount();
        this.commentList = review.getCommentList().stream().map(Comment::toDTO).toList();
    }

    public Review toEntity(User user) {
        Review review = new Review();
        review.setId(this.id);
        review.setTitle(this.title);
        review.setPhotos(this.photos);
        review.setContent(this.content);
        review.setHeartList(this.heartList);
        review.setViewCount(this.viewCount);
        review.setHeartCount(this.heartCount);
        review.setUser(user);
        return review;
    }

    public static ResponseReview from(Review review) {
        ResponseReview response = new ResponseReview();
        response.setId(review.getId());
        response.setTitle(review.getTitle());
        response.setPhotos(review.getPhotos());
        response.setContent(review.getContent());
        response.setHeartList(review.getHeartList());
        response.setViewCount(review.getViewCount());
        response.setHeartCount(review.getHeartCount());
        return response;
    }
}
