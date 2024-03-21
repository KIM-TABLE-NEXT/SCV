package com.sparta.scv.board.dto;

import lombok.Getter;

@Getter
public class BoardRequest {

    private String name;
    private String description;
    private String color;

    public BoardRequest(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }
}
