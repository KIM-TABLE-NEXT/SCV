package com.sparta.scv.comment.controller;

import com.sparta.scv.annotation.BoardMemberOnly;
import com.sparta.scv.boardcolumn.BoardIdRequestDto;
import com.sparta.scv.comment.dto.request.CommentIdRequest;
import com.sparta.scv.comment.dto.request.CommentRequest;
import com.sparta.scv.comment.dto.response.CommentResponse;
import com.sparta.scv.comment.dto.response.CommentStatusResponse;
import com.sparta.scv.comment.service.CommentService;
import com.sparta.scv.global.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Comment", description = "Comment API")
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @BoardMemberOnly
    @PostMapping
    @Operation(summary = "댓글 생성", description = "카드 ID를 통해 댓글을 생성한다.")
    public ResponseEntity<CommentStatusResponse> createComment(@RequestBody CommentRequest commentRequest, @AuthenticationPrincipal
        UserDetailsImpl userDetails){
        return ResponseEntity.status(201).body(commentService.createComment(commentRequest, userDetails.getUser()));
    }

    @BoardMemberOnly
    @GetMapping
    @Operation(summary = "댓글 조회", description = "카드 ID를 통해 댓글을 조회한다.")
    public List<CommentResponse> getComment(@RequestBody CommentIdRequest commentIdRequest){
        return commentService.getComment(commentIdRequest);
    }

    @BoardMemberOnly
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글 ID를 통해 댓글을 수정한다.")
    public ResponseEntity<CommentStatusResponse> updateComment(@RequestBody CommentRequest commentRequest, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(201).body(commentService.updateComment(commentId, commentRequest, userDetails.getUser()));
    }

    @BoardMemberOnly
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 ID를 통해 댓글을 삭제한다.")
    public ResponseEntity<CommentStatusResponse> deleteComment(@RequestBody BoardIdRequestDto boardIdRequestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(200).body(commentService.deleteComment(commentId, userDetails.getUser()));
    }
}
