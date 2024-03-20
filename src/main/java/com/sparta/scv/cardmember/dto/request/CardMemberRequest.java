package com.sparta.scv.cardmember.dto.request;

import com.sparta.scv.board.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardMemberRequest implements BoardIdHolder {
    private Long boardId;
    private Long cardId;
    private Long memberId;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
