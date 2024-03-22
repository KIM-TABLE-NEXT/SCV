package com.sparta.scv.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {

    private Long commentId;
    private String content;

}
