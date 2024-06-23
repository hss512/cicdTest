package com.example.easyplan.domain.dto;

import com.example.easyplan.domain.entity.review.Photo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewUpdateRequest {
    private String title;
    private List<Photo> photos=new ArrayList<>();
    private String content;
}
