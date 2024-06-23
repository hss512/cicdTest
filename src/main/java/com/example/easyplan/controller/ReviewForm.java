package com.example.easyplan.controller;

import com.example.easyplan.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class ReviewForm {
    private Long id;

    private String title;

    private List<MultipartFile> photoFiles;

    private String content;

    private BaseEntity baseEntity;

}
