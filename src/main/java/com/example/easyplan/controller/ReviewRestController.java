package com.example.easyplan.controller;

import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.service.FileStorageService;
import com.example.easyplan.service.ReviewServiceImpl;
import com.example.easyplan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ReviewRestController {

    private final ReviewServiceImpl reviewService;
    private final FileStorageService fileStorageService;

    @GetMapping("/api/reviews/new")
    public ResponseEntity<?> createReviewForm() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ReviewForm reviewForm = new ReviewForm();
        return ResponseEntity.ok(reviewForm);
    }

    @PostMapping("/api/reviews")
    public ResponseEntity<?> submitReviewForm(
            ReviewForm reviewForm,
            @RequestParam(name = "photoFiles", required = false) List<MultipartFile> photoFiles) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        if (photoFiles != null && !photoFiles.isEmpty()) {
            reviewService.save(reviewForm, photoFiles, Long.parseLong(userId));
        } else {
            reviewService.save(reviewForm, Long.parseLong(userId));
        }
        return ResponseEntity.ok().body("리뷰 제출 성공");
    }

    @GetMapping("/api/photos/{fileName:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("fileName") String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/api/reviews/{reviewId}/editForm")
    public ResponseEntity<?> editReview(@PathVariable("reviewId") Long reviewId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ResponseReview responseReview = reviewService.findReviewByReviewId(reviewId);
        return ResponseEntity.ok(responseReview);
    }

    @PostMapping("/api/reviews/{reviewId}/edit")
    public ResponseEntity<?> editReview(@PathVariable("reviewId") Long reviewId, ReviewForm reviewForm,
                                        @RequestParam(name = "photoFiles", required = false) List<MultipartFile> photoFiles) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        boolean success = reviewService.updateReview(Long.parseLong(userId), reviewId, reviewForm, photoFiles);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰 또는 사용자를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok().body("리뷰 수정 성공");
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        boolean success = reviewService.deleteReview(Long.parseLong(userId), reviewId);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰 또는 사용자를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok().body("리뷰 삭제 성공");
    }

    @GetMapping("/reviews")
    @CrossOrigin
    public ResponseEntity<Page<ResponseReview>> getReviews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("heartCount").descending());
        Page<ResponseReview> allReviews = reviewService.findAllReviews(pageRequest);
        return ResponseEntity.ok(allReviews);
    }

    @GetMapping("/api/reviews/{reviewId}/writer")
    public ResponseEntity<?> checkWriter(@PathVariable("reviewId") Long reviewId){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Long writerId = reviewService.findReviewByReviewId(reviewId).getUserId();
        if(userId.equals(writerId)){
            log.info("check_writer: " + 1);
            return ResponseEntity.ok(1);
        }else{
            log.info("check_writer: " + 0);
            return ResponseEntity.ok(0);
        }
    }
}