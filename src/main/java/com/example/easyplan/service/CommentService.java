package com.example.easyplan.service;

import com.example.easyplan.domain.dto.ResponseCommentDTO;
import com.example.easyplan.domain.entity.review.Comment;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentService {

    private final CommentRepository commentRepository;

    public ResponseCommentDTO createComment(User user, String content, Review review) {

        Comment comment = Comment.builder()
                .review(review)
                .user(user)
                .content(content)
                .build();

        return commentRepository.save(comment).toDTO(comment);
    }


    public List<ResponseCommentDTO> getCommentList(Review review) {

        return review.getCommentList().stream().map(Comment::toDTO).toList();
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        if(comment.getUser().getId().equals(userId)){
            commentRepository.delete(comment);
        }
    }
}
