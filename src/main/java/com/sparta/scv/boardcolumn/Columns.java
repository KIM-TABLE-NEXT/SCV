package com.sparta.scv.boardcolumn;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Columns {

    private List<BoardColumnResponseDto> boardColumns = new ArrayList<>();

    public Columns(List<BoardColumnResponseDto> boardColumns) {
        this.boardColumns = boardColumns;
    }
}
