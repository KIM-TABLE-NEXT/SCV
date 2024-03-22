package com.sparta.scv.boardcolumn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultResponseDto {

    private Integer code;
    private String status;
    private Long boardColumnId;

    public ResultResponseDto(Integer code, String status, Long boardColumnId) {
        this.code = code;
        this.status = status;
        this.boardColumnId = boardColumnId;
    }
}
