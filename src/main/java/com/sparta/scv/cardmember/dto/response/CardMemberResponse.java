package com.sparta.scv.cardmember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CardMemberResponse {

    private Long cardId;
    private Long memberId;

}
