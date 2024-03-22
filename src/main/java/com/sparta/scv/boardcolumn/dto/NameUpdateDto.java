package com.sparta.scv.boardcolumn.dto;

import com.sparta.scv.aop.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NameUpdateDto implements BoardIdHolder {

    private Long boardId;
    private String boardColumnName;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
