package com.example.easyplan.controller;

import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.service.HeartService;
import com.example.easyplan.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class HeartRestController {
    private final HeartService heartService;
    private final ReviewService reviewService;

    @PostMapping("/reviews/{reviewId}/hearts")
    public ResponseEntity<?> toggleHeart(@PathVariable("reviewId") Long reviewId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        heartService.toggleHeart(reviewId, Long.parseLong(userId));
        log.info("토글 성공");
        return ResponseEntity.ok().body("하트 토글 성공");
    }

    @GetMapping("/reviews/{reviewId}/hearts/count")
    public ResponseEntity<Long> heartCnt(@PathVariable("reviewId") Long reviewId) {
        Long heartCnt = heartService.countHearts(reviewId);
        return ResponseEntity.ok(heartCnt);
    }

    @GetMapping("/users/hearts")
    public ResponseEntity<?> getUserHearts() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        List<Review> heartedReviews = heartService.findHeartsByUserId(Long.parseLong(userId));
        return ResponseEntity.ok().body(heartedReviews);
    }

    @GetMapping("/reviews/{reviewId}/user-heart")
    public ResponseEntity<?> getUserHeartStatus(@PathVariable("reviewId") Long reviewId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        boolean userHasHearted = heartService.userHasHearted(reviewId, Long.parseLong(userId));
        return ResponseEntity.ok(userHasHearted);
    }
}