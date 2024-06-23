package com.example.easyplan.service;

import com.example.easyplan.domain.entity.review.Heart;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.HeartRepository;
import com.example.easyplan.repository.ReviewRepository;
import com.example.easyplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void toggleHeart(Long reviewId, Long userId) {
        Review review = reviewRepository.findByReviewId(reviewId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자만 하트를 누를 수 있습니다."));

        Heart heart = heartRepository.findByReviewIdAndUserId(reviewId, userId);
        if (heart != null) {
            heartRepository.delete(heart);
            review.decreaseHeartCount();
        } else {
            heart = new Heart(review, user);
            heartRepository.save(heart);
            review.increaseHeartCount();
        }
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countHearts(Long reviewId) {
        return heartRepository.countByReviewId(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findHeartsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자만 볼 수 있습니다."));
        return heartRepository.findByUserId(userId).stream()
                .map(Heart::getReview)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasHearted(Long reviewId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return heartRepository.findByReviewIdAndUserId(reviewId, userId) != null;
    }
}
