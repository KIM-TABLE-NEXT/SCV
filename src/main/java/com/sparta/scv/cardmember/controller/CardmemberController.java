package com.sparta.scv.cardmember.controller;

import com.sparta.scv.cardmember.dto.request.CardMemberIdRequest;
import com.sparta.scv.cardmember.dto.request.CardMemberRequest;
import com.sparta.scv.cardmember.dto.response.CardMemberResponse;
import com.sparta.scv.cardmember.dto.response.CardMemberStatusResponse;
import com.sparta.scv.cardmember.service.CardmemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cardmember", description = "Cardmember API")
@RequiredArgsConstructor
@RequestMapping("/v1/cardmembers")
public class CardmemberController {

    private final CardmemberService cardmemberService;

    @PostMapping
    @Operation(summary = "작업자 추가", description = "작업자를 추가한다.")
    public ResponseEntity<CardMemberStatusResponse> createCardMember(@RequestBody CardMemberRequest cardMemberRequest, Long userId){
        return ResponseEntity.status(200).body(cardmemberService.createCardMember(cardMemberRequest, userId));
    }

    @GetMapping
    @Operation(summary = "작업자 조회", description = "카드 ID를 통해 작업자를 조회한다.")
    public List<CardMemberResponse> getCardMember(@RequestBody CardMemberIdRequest cardMemberIdRequest){
        return cardmemberService.getCardMember(cardMemberIdRequest);
    }

    @DeleteMapping
    @Operation(summary = "작업자 조회", description = "카드 ID를 통해 작업자를 삭제한다.")
    public ResponseEntity<CardMemberStatusResponse> deleteCardMember(@RequestBody CardMemberRequest cardMemberRequest, Long userId){
        return ResponseEntity.status(200).body(cardmemberService.deleteCardMember(cardMemberRequest, userId));
    }
}
