package com.sparta.scv.card.controller;

import com.sparta.scv.card.dto.request.CardRequest;
import com.sparta.scv.card.dto.response.CardResponse;
import com.sparta.scv.card.dto.response.CardStatusResponse;
import com.sparta.scv.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @Operation(summary = "카드 생성", description = "카드를 생성한다.")
    public ResponseEntity<CardStatusResponse> createCard(@RequestBody CardRequest cardRequest, Long userId){
        return ResponseEntity.status(200).body(cardService.createCard(cardRequest, userId));
    }

    @GetMapping("/{cardId}")
    @Operation(summary = "카드 조회", description = "카드 ID를 통해 카드를 조회한다.")
    public CardResponse getCard(@PathVariable Long cardId){
        return cardService.getCard(cardId);
    }

    @PutMapping("/{cardId}")
    @Operation(summary = "카드 수정", description = "카드 ID를 통해 카드를 수정한다.")
    public ResponseEntity<CardStatusResponse> updateCard(@PathVariable Long cardId,@RequestBody CardRequest cardRequest, Long userId){
        return ResponseEntity.status(200).body(cardService.updateCard(cardId, cardRequest, userId));
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "카드 삭제", description = "카드 ID를 통해 카드를 삭제한다.")
    public ResponseEntity<CardStatusResponse> deleteCard(@PathVariable Long cardId, Long userId){
        return ResponseEntity.status(200).body(cardService.deleteCard(cardId, userId));
    }
}
