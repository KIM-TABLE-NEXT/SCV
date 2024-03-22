package com.sparta.scv.cardmember.dto.request;

import com.sparta.scv.aop.BoardIdHolder;
import lombok.Getter;

@Getter
public class CardMemberIdRequest implements BoardIdHolder {

    private Long boardId;
    private Long cardId;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
