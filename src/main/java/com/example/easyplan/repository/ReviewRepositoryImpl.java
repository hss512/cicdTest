package com.example.easyplan.repository;

import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.entity.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ReviewRepositoryImpl implements ReviewRepository{
    private final EntityManager em;
    private final HeartRepository heartRepository;

    @Override
    public void save(Review review){
        em.persist(review);
    }

    @Override
    public Review findReviewId(Long reviewId){
        return em.find(Review.class, reviewId);
    }

    @Override
    public Review findByReviewId(Long reviewId) {
        Review myReview = em.createQuery("SELECT r FROM Review r WHERE r.id = :reviewId", Review.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
        return myReview;
    }

    @Override
    @Transactional
    public List<Review> findReviewsByUserId(Long userId){
        List<Review> myReview = em.createQuery("SELECT r FROM Review r WHERE r.user.id = :userId", Review.class)
                .setParameter("userId", userId)
                .getResultList();
        return myReview;
    }

    @Override
    @Transactional
    public void delete(Review review) {
        // 삭제하기 전에 Heart 엔티티들 삭제
        heartRepository.deleteByReviewId(review.getId());

        if (em.contains(review)) {
            em.remove(review);
        } else {
            em.remove(em.merge(review));
        }
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        int totalRows = ((Number) em.createQuery("SELECT COUNT(r) FROM Review r").getSingleResult()).intValue();

        TypedQuery<Review> query = em.createQuery("SELECT r FROM Review r ORDER BY r.heartCount DESC", Review.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Review> reviews = query.getResultList();

        return new PageImpl<>(reviews, pageable, totalRows);
    }

}
