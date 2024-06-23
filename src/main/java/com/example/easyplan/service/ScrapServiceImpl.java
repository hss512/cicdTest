package com.example.easyplan.service;

import com.example.easyplan.domain.entity.scrap.Scrap;
import com.example.easyplan.repository.ScrapRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService{
    private final ScrapRepository scrapRepository;

    @Override
    @Transactional
    public Scrap saveScrap(Scrap scrap) {
        return scrapRepository.save(scrap);
    }

    @Override
    public List<Scrap> findScrapsByUserId(Long userId, int page, int size) {
        return scrapRepository.findListByUserId(userId, page, size);
    }

    @Override
    @Transactional
    public Optional<Scrap> findScrapByUserIdAndReviewId(Long userId, Long reviewId) {
        return scrapRepository.findByUserIdAndReviewId(userId, reviewId);
    }

    @Override
    @Transactional
    public void deleteScrapByUserIdAndReviewId(Long userId, Long reviewId) {
        scrapRepository.deleteByUserIdAndReviewId(userId, reviewId);
    }
}
