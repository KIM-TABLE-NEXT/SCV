package com.sparta.scv.card.controller;

import com.sparta.scv.annotation.BoardMemberOnly;
import com.sparta.scv.boardcolumn.BoardIdRequestDto;
import com.sparta.scv.card.dto.request.CardRequest;
import com.sparta.scv.card.dto.request.CardUpdateRequest;
import com.sparta.scv.card.dto.response.CardResponse;
import com.sparta.scv.card.dto.response.CardStatusResponse;
import com.sparta.scv.card.service.CardService;
import com.sparta.scv.global.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Card", description = "Card API")
@RequiredArgsConstructor
@RequestMapping("/v1/cards")
public class CardController {

    private final CardService cardService;

    @BoardMemberOnly
    @PostMapping
    @Operation(summary = "카드 생성", description = "카드를 생성한다.")
    public ResponseEntity<CardStatusResponse> createCard(@RequestBody CardRequest cardRequest, @AuthenticationPrincipal
        UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardRequest, userDetails.getUser()));
    }

    @BoardMemberOnly
    @GetMapping("/{cardId}")
    @Operation(summary = "카드 조회", description = "카드 ID를 통해 카드를 조회한다.")
    public CardResponse getCard(@RequestBody BoardIdRequestDto boardIdRequestDto, @PathVariable Long cardId){
        return cardService.getCard(cardId);
    }

    @BoardMemberOnly
    @PutMapping("/{cardId}")
    @Operation(summary = "카드 내용 수정", description = "카드 ID를 통해 카드를 내용을 수정한다.")
    public ResponseEntity<CardStatusResponse> updateCard(@RequestBody CardUpdateRequest cardUpdateRequest, @PathVariable Long cardId, @AuthenticationPrincipal
    UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.updateCard(cardId, cardUpdateRequest, userDetails.getUser()));
    }

    @BoardMemberOnly
    @PutMapping("/{cardId}/boardcolumns/{boardcolumnId}")
    @Operation(summary = "카드 컬럼 변경", description = "카드 ID와 컬럼 ID를 통해 카드를 이동한다.")
    public ResponseEntity<CardStatusResponse> moveCard(@RequestBody BoardIdRequestDto boardIdRequestDto, @PathVariable Long cardId,
        @PathVariable Long boardcolumnId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.moveCard(cardId, boardcolumnId, userDetails.getUser()));
    }

    @BoardMemberOnly
    @DeleteMapping("/{cardId}")
    @Operation(summary = "카드 삭제", description = "카드 ID를 통해 카드를 삭제한다.")
    public ResponseEntity<CardStatusResponse> deleteCard(@RequestBody BoardIdRequestDto boardIdRequestDto, @PathVariable Long cardId, @AuthenticationPrincipal
    UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(cardService.deleteCard(cardId, userDetails.getUser()));
    }
}
