package com.example.easyplan.repository;

import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {
    public void save(Review review);
    public Review findReviewId(Long reviewId);
    public Review findByReviewId(Long reviewId);

    public List<Review> findReviewsByUserId(Long userId);

    public void delete(Review review);

    public Page<Review> findAll(Pageable pageable);
    //public Review findReviewsByReviewIdAndUserId(Long reviewId, Long userEmail);

    // public void updateReviewByReviewIdAndUserId(Long reviewId, Long userId, String title, String content);

    //public void deleteReviewByUserId(Long reviewId, Long userId);

    //좋아요순, 조회순, 최신순 조회
    //public List<Review> sortedReview(String sortBy, int page, int size);

}