package com.sparta.scv.cardmember.controller;

import com.sparta.scv.annotation.BoardMemberOnly;
import com.sparta.scv.cardmember.dto.request.CardMemberIdRequest;
import com.sparta.scv.cardmember.dto.request.CardMemberRequest;
import com.sparta.scv.cardmember.dto.response.CardMemberResponse;
import com.sparta.scv.cardmember.dto.response.CardMemberStatusResponse;
import com.sparta.scv.cardmember.service.CardmemberService;
import com.sparta.scv.global.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Cardmember 관리", description = "Cardmember 관리에 관련된 API")
@RequiredArgsConstructor
@RequestMapping("/v1/cardmembers")
public class CardmemberController {

    private final CardmemberService cardmemberService;

    @BoardMemberOnly
    @PostMapping
    @Operation(summary = "작업자 추가", description = "작업자를 추가한다.")
    public ResponseEntity<CardMemberStatusResponse> createCardMember(
        @RequestBody CardMemberRequest cardMemberRequest, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cardmemberService.createCardMember(cardMemberRequest, userDetails.getUser()));
    }

    @BoardMemberOnly
    @GetMapping
    @Operation(summary = "작업자 조회", description = "카드 ID를 통해 작업자를 조회한다.")
    public List<CardMemberResponse> getCardMember(
        @RequestBody CardMemberIdRequest cardMemberIdRequest) {
        return cardmemberService.getCardMember(cardMemberIdRequest);
    }

    @BoardMemberOnly
    @DeleteMapping
    @Operation(summary = "작업자 삭제", description = "카드 ID를 통해 작업자를 삭제한다.")
    public ResponseEntity<CardMemberStatusResponse> deleteCardMember(
        @RequestBody CardMemberRequest cardMemberRequest, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(cardmemberService.deleteCardMember(cardMemberRequest, userDetails.getUser()));
    }
}
