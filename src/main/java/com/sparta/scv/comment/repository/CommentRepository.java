package com.sparta.scv.comment.repository;

import com.sparta.scv.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCardId(Long cardId);
}
