package com.example.easyplan.controller;//package com.example.easyplan.controller;
//
//import com.example.easyplan.domain.dto.ResponseReview;
//import com.example.easyplan.domain.entity.review.Review;
//import com.example.easyplan.domain.entity.user.User;
//import com.example.easyplan.service.ReviewService;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.hibernate.query.sqm.tree.SqmNode.log;
//
//@Controller
//@RequiredArgsConstructor
//@Log4j2
//public class MypageController {
//    final private ReviewService reviewService;
//
//    @GetMapping("/mypage")
//    public String myPage(Model model){
//        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
//        List<ResponseReview> myReview= reviewService.findReviewsByUserId(userId);
//        model.addAttribute("myReview", myReview);
//        return "mypage";
//    }

//    @GetMapping("/mypage/reviews")
//    @ResponseBody
//    public List<Review> getMyReview(){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Review> myReview= reviewService.findReviewsByUserId(user.getId());
//        return myReview;
//    }
//
//}