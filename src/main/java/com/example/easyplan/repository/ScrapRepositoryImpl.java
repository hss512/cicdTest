package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.scrap.Scrap;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScrapRepositoryImpl implements ScrapRepository{
    private final EntityManager em;

    @Override
    public Scrap save(Scrap scrap){
        em.persist(scrap);
        return scrap;
    }

    @Override
    public List<Scrap> findListByUserId(Long userId, int page, int size){
        int start=page*size;
        List<Scrap> userList= em.createQuery("SELECT s FROM Scrap s WHERE s.user.id= :userId", Scrap.class)
                .setParameter("userId", userId)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();

        return userList;
    }


    @Override
    public Optional<Scrap> findByUserIdAndReviewId(Long userId, Long reviewId){
        List<Scrap> result = em.createQuery("SELECT s FROM Scrap s WHERE s.user.id = :userId AND s.review.id = :reviewId", Scrap.class)
                .setParameter("userId", userId)
                .setParameter("reviewId", reviewId)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public void deleteByUserIdAndReviewId(Long userId, Long reviewId){
        List<Scrap> scraps =em.createQuery("SELECT s FROM Scrap s WHERE s.user.id = :userId AND s.review.id = :reviewId", Scrap.class)
                .setParameter("userId", userId)
                .setParameter("reviewId", reviewId)
                .getResultList();

        for(Scrap scrap:scraps){
            em.remove(scrap);
        }
    }



}
