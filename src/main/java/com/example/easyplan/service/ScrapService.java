package com.example.easyplan.service;

import com.example.easyplan.domain.entity.scrap.Scrap;

import java.util.List;
import java.util.Optional;

public interface ScrapService {
    public Scrap saveScrap(Scrap scrap);
    public List<Scrap> findScrapsByUserId(Long userId, int page, int size);
    public Optional<Scrap> findScrapByUserIdAndReviewId(Long userId, Long reviewId);
    public void deleteScrapByUserIdAndReviewId(Long userId, Long reviewId);

}
