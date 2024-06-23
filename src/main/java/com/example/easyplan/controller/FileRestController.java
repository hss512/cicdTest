package com.example.easyplan.controller;

import com.example.easyplan.service.FileStorageService;
import com.example.easyplan.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api2/files")
@RequiredArgsConstructor
public class FileRestController {
    private final FileStorageService fileStorageService;
    private final ReviewService reviewService;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/reviews")
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
}


