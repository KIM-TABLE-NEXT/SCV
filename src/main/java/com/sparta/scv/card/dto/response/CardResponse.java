package com.sparta.scv.card.dto.response;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CardResponse {
    private Long cardId;
    private String title;
    private String description;
    private String color;
}
