package com.sparta.scv.card.dto.response;

import com.sparta.scv.card.entity.Card;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardResponse {

    private Long cardId;
    private String title;
    private String description;
    private String color;
    private String startDate;
    private String endDate;
    private String nickname;

    public CardResponse(Card card) {
        this.cardId = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.color = card.getColor();
        this.startDate = card.getStartDate();
        this.endDate = card.getEndDate();
        this.nickname = card.getOwner().getNickname();
    }
}
