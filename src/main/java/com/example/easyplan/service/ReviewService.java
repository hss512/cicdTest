package com.example.easyplan.service;

import com.example.easyplan.controller.ReviewForm;
import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    public void save(ReviewForm review, List<MultipartFile> photoFiles, Long userId);

    public void save(ReviewForm reviewForm, Long userId);

    public ResponseReview findReviewByReviewId(Long reviewId);

    public List<ResponseReview> findReviewsByUserId(Long userId);

    public boolean updateReview(Long userId, Long reviewId, ReviewForm reviewForm, List<MultipartFile> photoFiles);

    public boolean deleteReview(Long userId, Long reviewId);

    public Page<ResponseReview> findAllReviews(PageRequest pageRequest);

}