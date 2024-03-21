package com.sparta.scv.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardDto {
    private final Long boardId;
    private final String title;
}
