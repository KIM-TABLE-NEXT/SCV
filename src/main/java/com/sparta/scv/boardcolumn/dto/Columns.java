package com.sparta.scv.boardcolumn.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Columns {

    private List<BoardColumnResponseDto> boardColumns = new ArrayList<>();

    public Columns(List<BoardColumnResponseDto> boardColumns) {
        this.boardColumns = boardColumns;
    }
}
