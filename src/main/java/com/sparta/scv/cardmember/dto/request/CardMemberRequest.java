package com.sparta.scv.cardmember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardMemberRequest {
    private Long cardId;
    private Long memberId;
}
