package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Heart;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface HeartRepository {
    public Heart save(Heart heart);
    public void delete(Heart heart);
    //public void toggleHeart(Long reviewId, Long userId);
    public Long countByReviewId(Long reviewId);
    public List<Heart> findByUserId(Long userId);
    public Heart findByReviewIdAndUserId(Long reviewId, Long userId);
    public List<Heart> findHeartsByReviewAndUser(Long reviewId, Long userId);
    public void deleteByReviewId(Long reviewId);
    //public Heart findByReviewAndUser(Review review, User user);
}
