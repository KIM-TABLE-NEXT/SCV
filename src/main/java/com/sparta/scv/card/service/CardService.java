package com.sparta.scv.card.service;

import com.sparta.scv.boardcolumn.BoardColumn;
import com.sparta.scv.boardcolumn.BoardColumnRepository;
import com.sparta.scv.card.dto.request.CardRequest;
import com.sparta.scv.card.dto.response.CardResponse;
import com.sparta.scv.card.dto.response.CardStatusResponse;
import com.sparta.scv.card.entity.Card;
import com.sparta.scv.card.repository.CardRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final UserRepository userRepository;

    @Transactional
    public CardStatusResponse createCard(CardRequest cardRequest, User user) {
        BoardColumn boardColumn = getColumnById(cardRequest.getColumnId());

        Card card = cardRepository.save(new Card(cardRequest.getTitle(), cardRequest.getDescription(), cardRequest.getColor(), boardColumn, user));
        return new CardStatusResponse(201, "CREATED", card.getId());
    }

    public CardResponse getCard(Long cardId) {
        Card card = getCardById(cardId);
        return new CardResponse(card.getId(), card.getTitle(), card.getDescription(), card.getColor());
    }

    @Transactional
    public CardStatusResponse updateCard(Long cardId, CardRequest cardRequest, User user) {
        Card card = getCardById(cardId);
        BoardColumn boardColumn = getColumnById(cardRequest.getColumnId());

        if(!user.equals(card.getOwner()))
            throw new IllegalArgumentException("해당 카드를 수정할 권한이 없습니다.");

        card.update(cardRequest, boardColumn);
        return new CardStatusResponse(201, "OK", card.getId());
    }

    @Transactional
    public CardStatusResponse deleteCard(Long cardId, User user) {
        Card card = getCardById(cardId);
        if(!user.equals(card.getOwner()))
            throw new IllegalArgumentException("해당 카드를 삭제할 권한이 없습니다.");
        cardRepository.deleteById(cardId);
        return new CardStatusResponse(200, "OK", card.getId());
    }

    public BoardColumn getColumnById(Long columnId){
        return boardColumnRepository.findById(columnId).orElseThrow(
            ()-> new NullPointerException("해당 Column이 존재하지 않습니다.")
        );
    }

    public Card getCardById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(
            ()-> new NullPointerException("해당 카드가 존재하지 않습니다.")
        );
    }
}
