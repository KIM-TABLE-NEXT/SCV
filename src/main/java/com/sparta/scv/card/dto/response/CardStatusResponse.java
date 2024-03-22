package com.sparta.scv.card.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardStatusResponse {

    private int code;
    private String status;
    private Long cardId;
}
