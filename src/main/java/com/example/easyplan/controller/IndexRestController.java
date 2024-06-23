package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.KakaoMessage;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.UserRepository;
import com.example.easyplan.service.MessageService;
import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api2")
@Log4j2
public class IndexRestController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    private final ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<Page<ResponseReview>> mainPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("heartCount").descending());
        Page<ResponseReview> allReviews = reviewService.findAllReviews(pageRequest);
        return ResponseEntity.ok(allReviews);
    }

    @GetMapping("/reviews/{reviewId}/details")
    public ResponseReview details(@PathVariable("reviewId") Long reviewId) {
        ResponseReview responseReview = reviewService.findReviewByReviewId(reviewId);
        log.info(responseReview);
        return responseReview;
    }

    @GetMapping("/test")
    public String test(){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("user_id: " + userId);
        User user = userRepository.findById(Long.parseLong(userId)).get();
        KakaoMessage kakaoMessage = new KakaoMessage();
        kakaoMessage.setBtnTitle("testBtn");
        kakaoMessage.setText("test");
        kakaoMessage.setObjType("feed");
        messageService.sendMessage(user.getAccessToken(), kakaoMessage);

        return "ok";
    }
}
