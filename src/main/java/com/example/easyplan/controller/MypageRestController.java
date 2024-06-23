package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.service.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class MypageRestController {
    private final ReviewService reviewService;

    @GetMapping("/mypage/reviews")
    public ResponseEntity<?> mypage(HttpServletResponse response) throws IOException {
        log.info("pageAPI");
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ResponseReview> myReview = reviewService.findReviewsByUserId(Long.parseLong(userId));
        log.info("userId: " + userId);
        log.info("myReview: " + myReview.toString());

        return ResponseEntity.ok(myReview);
    }
}