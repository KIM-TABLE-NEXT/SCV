package com.sparta.scv.comment.dto.request;

import com.sparta.scv.aop.BoardIdHolder;
import lombok.Getter;

@Getter
public class CommentRequest implements BoardIdHolder {

    private Long boardId;
    private String content;
    private Long cardId;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
