package com.sparta.scv.boardcolumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnRequestDto {

    private Long boardId;
    private String columnName;
    private Long position;

}
