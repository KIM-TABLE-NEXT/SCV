package com.sparta.scv.card.dto.request;

import com.sparta.scv.board.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardRequest implements BoardIdHolder {
    private Long boardId;
    private Long columnId;
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
