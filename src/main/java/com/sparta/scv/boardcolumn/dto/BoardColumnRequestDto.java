package com.sparta.scv.boardcolumn.dto;

import com.sparta.scv.aop.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnRequestDto implements BoardIdHolder {

    private Long boardId;
    private String columnName;
    private Long position;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
