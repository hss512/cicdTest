package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}