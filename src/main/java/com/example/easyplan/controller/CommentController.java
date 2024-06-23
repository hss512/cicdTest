package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.RequestCommentDTO;
import com.example.easyplan.domain.dto.ResponseCommentDTO;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.ReviewRepository;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @PostMapping("/api/comment")
    public ResponseEntity<?> createComment(@RequestBody RequestCommentDTO comment){
        log.info("review_id: " + comment.getReviewId());
        log.info("content: " + comment.getContent());
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(userId)).get();
        Review review = reviewRepository.findByReviewId(Long.parseLong(comment.getReviewId()));
        ResponseCommentDTO response = commentService.createComment(user, comment.getContent(), review);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/comment/{reviewId}")
    public ResponseEntity<?> getCommentList(@PathVariable String reviewId){
        Review review = reviewRepository.findByReviewId(Long.parseLong(reviewId));
        return ResponseEntity.ok(commentService.getCommentList(review));
    }

    @DeleteMapping("/api/comment/{reviewId}")
    public String deleteComment(@PathVariable String reviewId){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("comment_id: " + reviewId);
        Long commentId = Long.parseLong(reviewId);

        commentService.deleteComment(userId, commentId);
        return "ok";
    }
}
