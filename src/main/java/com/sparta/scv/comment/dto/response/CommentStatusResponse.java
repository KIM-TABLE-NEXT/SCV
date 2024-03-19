package com.sparta.scv.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentStatusResponse {
    private int code;
    private String status;
    private Long commentId;

}
