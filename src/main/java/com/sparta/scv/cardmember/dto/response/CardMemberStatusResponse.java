package com.sparta.scv.cardmember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CardMemberStatusResponse {
    private int code;
    private String status;
    private Long cardId;
    private Long memberId;
}
