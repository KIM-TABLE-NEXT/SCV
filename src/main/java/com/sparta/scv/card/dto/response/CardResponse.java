package com.sparta.scv.card.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardResponse {
    private Long cardId;
    private String title;
    private String description;
    private String color;
}
