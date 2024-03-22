package com.sparta.scv.comment.service;

import com.sparta.scv.card.entity.Card;
import com.sparta.scv.card.repository.CardRepository;
import com.sparta.scv.comment.dto.request.CommentIdRequest;
import com.sparta.scv.comment.dto.request.CommentRequest;
import com.sparta.scv.comment.dto.response.CommentResponse;
import com.sparta.scv.comment.dto.response.CommentStatusResponse;
import com.sparta.scv.comment.entity.Comment;
import com.sparta.scv.comment.repository.CommentRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentStatusResponse createComment(CommentRequest commentRequest, User user) {
        user = getUserById(user.getId());
        Card card = getCardById(commentRequest.getCardId());

        Comment comment = commentRepository.save(new Comment(commentRequest, user, card));
        return new CommentStatusResponse(201, "CREATED", comment.getId());
    }

    public List<CommentResponse> getComment(CommentIdRequest commentIdRequest) {
        List<Comment> commentList = commentRepository.findAllByCardId(commentIdRequest.getCardId());
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseList.add(new CommentResponse(comment.getId(), comment.getContent()));
        }

        return commentResponseList;
    }

    @Transactional
    public CommentStatusResponse updateComment(Long commentId, CommentRequest commentRequest,
        User user) {
        Comment comment = getCommentById(commentId);
        user = getUserById(user.getId());

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("해당 댓글을 수정할 권한이 없습니다.");
        }

        comment.update(commentRequest.getContent());
        return new CommentStatusResponse(201, "OK", comment.getId());
    }

    @Transactional
    public CommentStatusResponse deleteComment(Long commentId, User user) {
        Comment comment = getCommentById(commentId);
        user = getUserById(user.getId());

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("해당 댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.deleteById(comment.getId());
        return new CommentStatusResponse(200, "OK", comment.getId());
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
            () -> new NullPointerException("해당 카드가 존재하지 않습니다.")
        );
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new NullPointerException("해당 유저가 존재하지 않습니다.")
        );
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );
    }
}
