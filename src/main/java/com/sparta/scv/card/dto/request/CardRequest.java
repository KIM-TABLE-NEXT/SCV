package com.sparta.scv.card.dto.request;

import lombok.Getter;

@Getter
public class CardRequest {
    private String title;
    private String description;
    private String color;
    private Long columnId;

}
