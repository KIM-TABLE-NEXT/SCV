package com.sparta.scv.boardcolumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnResponseDto {

    private Long id;
    private String columnName;
    private Long position;

    public BoardColumnResponseDto(BoardColumn boardColumn) {
        this.id = boardColumn.getId();
        this.columnName = boardColumn.getColumnName();
        this.position = boardColumn.getPosition();
    }
}
