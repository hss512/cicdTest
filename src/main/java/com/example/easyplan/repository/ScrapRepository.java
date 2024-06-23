package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.scrap.Scrap;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository {

    public Scrap save(Scrap scrap);
    public List<Scrap> findListByUserId(Long userId, int page, int size);
    public Optional<Scrap> findByUserIdAndReviewId(Long userId, Long reviewId);
    public void deleteByUserIdAndReviewId(Long userId, Long reviewId);

}
