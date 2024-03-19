package com.sparta.scv.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentRequest {
    private String content;
    private Long cardId;
}
