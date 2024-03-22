package com.sparta.scv.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardResponse {

    private final String code;
    private final String status;
    private final Long boardId;
}
