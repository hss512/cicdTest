package com.example.easyplan.service;

import com.example.easyplan.domain.entity.review.Review;

import java.util.List;

public interface HeartService {
    void toggleHeart(Long reviewId, Long userId);
    Long countHearts(Long reviewId);
    List<Review> findHeartsByUserId(Long userId);
    boolean userHasHearted(Long reviewId, Long userId);
}
