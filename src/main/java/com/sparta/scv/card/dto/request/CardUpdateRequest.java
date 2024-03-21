package com.sparta.scv.card.dto.request;

import com.sparta.scv.board.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CardUpdateRequest implements BoardIdHolder {
    private Long boardId;
    private String title;
    private String description;
    private String color;
    private String startDate;
    private String endDate;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
