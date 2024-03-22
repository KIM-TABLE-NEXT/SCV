package com.sparta.scv.comment.dto.request;

import com.sparta.scv.aop.BoardIdHolder;
import lombok.Getter;

@Getter
public class CommentIdRequest implements BoardIdHolder {

    private Long boardId;
    private Long cardId;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
