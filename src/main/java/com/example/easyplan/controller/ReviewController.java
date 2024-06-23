package com.example.easyplan.controller;//package com.example.easyplan.controller;
//
//import com.example.easyplan.domain.dto.ResponseReview;
//import com.example.easyplan.domain.entity.review.Review;
//import com.example.easyplan.domain.entity.user.User;
//import com.example.easyplan.service.ReviewService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//@Log4j2
//public class ReviewController {
//    private final ReviewService reviewService;
//
//    @GetMapping("/reviews/new")
//    public ResponseEntity<?> createReviewForm(){
//        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
//        if(userId == null || userId.isEmpty()){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다."); //근데 안보임
//        }
//
//        ReviewForm reviewForm=new ReviewForm();
//        return ResponseEntity.ok(reviewForm);
//    }
//
//    @PostMapping("/reviews/new")
//    public ResponseEntity<?> submitReviewForm(ReviewForm reviewForm, @RequestParam(required = false)List<MultipartFile> photoFiles){
//        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        if(photoFiles!=null && !photoFiles.isEmpty()){
//            reviewService.save(reviewForm, photoFiles, Long.parseLong(userId));
//        }else{
//            reviewService.save(reviewForm, Long.parseLong(userId));
//        }
//
//        return ResponseEntity.ok().body("리뷰 제출 성공");
//
//    }
//
////    @GetMapping("/reviews/new")
////    public String createReviewForm(Model model){
////        model.addAttribute("reviewForm", new ReviewForm());
////        return "reviews/reviewForm";
////    }
////
////
////    @PostMapping("/reviews/new")
////    public String submitReviewForm(ReviewForm reviewForm) {
////        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
////        List<MultipartFile> photoFiles = reviewForm.getPhotoFiles();
////
////        if (photoFiles.isEmpty()) {
////            reviewService.save(reviewForm, userId);
////        } else {
////            reviewService.save(reviewForm, photoFiles, userId);
////        }
////
////        return "redirect:/mypage";
////    }
////
////
////    @GetMapping("myreview/{reviewId}/details")
////    public String reviewDetail(@PathVariable("reviewId") Long reviewId, @ModelAttribute ReviewForm reviewForm, Model model, Principal principal){
////        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        Review selectReview = reviewService.findReviewsByReviewIdAndUserId(reviewId, user.getId());
////
////        reviewForm.setTitle(selectReview.getTitle());
////        reviewForm.setContent(selectReview.getContent());
////
////        model.addAttribute("review", reviewForm);
////
////       return "reviews/reviewDetails";
////    }
////
////    @GetMapping("myreview/{reviewId}/edit")
////    public String updateReview(@PathVariable("reviewId")Long reviewId, Model model){
////        model.addAttribute("review", new ReviewForm());
////
////        return "reviews/updateReviewForm";
////    }
//
////    @PostMapping("myreview/{reviewId}/edit")
////    public String updateReview(@PathVariable Long reviewId, @ModelAttribute("form") ReviewForm reviewForm){
////        reviewService.updateReview(reviewId, reviewForm.getTitle(), reviewForm.getPhotoFiles(), reviewForm.getContent());
////
////        return "redirect:/mypage";
////    }
//
//}