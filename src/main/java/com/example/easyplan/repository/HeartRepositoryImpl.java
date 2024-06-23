package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Heart;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HeartRepositoryImpl implements HeartRepository {
    private final EntityManager em;

    @Override
    @Transactional
    public Heart save(Heart heart){
        em.persist(heart);
        return heart;
    }

    @Override
    @Transactional
    public void delete(Heart heart){
        em.remove(heart);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByReviewId(Long reviewId) {
        return em.createQuery("SELECT COUNT(h.id) FROM Heart h WHERE h.review.id = :reviewId", Long.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Heart> findByUserId(Long userId) {
        return em.createQuery("SELECT h FROM Heart h WHERE h.user.id = :userId", Heart.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Heart findByReviewIdAndUserId(Long reviewId, Long userId) {
        try {
            return em.createQuery("SELECT h FROM Heart h WHERE h.review.id = :reviewId AND h.user.id = :userId", Heart.class)
                    .setParameter("reviewId", reviewId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Heart> findHeartsByReviewAndUser(Long reviewId, Long userId) {
        return em.createQuery("SELECT h FROM Heart h WHERE h.review.id = :reviewId AND h.user.id = :userId", Heart.class)
                .setParameter("reviewId", reviewId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteByReviewId(Long reviewId) {
        em.createQuery("DELETE FROM Heart h WHERE h.review.id = :reviewId")
                .setParameter("reviewId", reviewId)
                .executeUpdate();
    }
}
