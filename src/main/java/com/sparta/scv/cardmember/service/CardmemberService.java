package com.sparta.scv.cardmember.service;

import com.sparta.scv.card.entity.Card;
import com.sparta.scv.card.repository.CardRepository;
import com.sparta.scv.cardmember.dto.request.CardMemberIdRequest;
import com.sparta.scv.cardmember.dto.request.CardMemberRequest;
import com.sparta.scv.cardmember.dto.response.CardMemberResponse;
import com.sparta.scv.cardmember.dto.response.CardMemberStatusResponse;
import com.sparta.scv.cardmember.entity.CardMember;
import com.sparta.scv.cardmember.repository.CardMemberRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardmemberService {

    private final CardMemberRepository cardMemberRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Transactional
    public CardMemberStatusResponse createCardMember(CardMemberRequest cardMemberRequest, User user) {
        Card card = getCardById(cardMemberRequest.getCardId());
        User member = getUserById(cardMemberRequest.getMemberId());

        if(!user.getId().equals(card.getOwner().getId()))
            throw new IllegalArgumentException("해당 카드에 작업자를 추가할 권한이 없습니다.");

        Optional<CardMember> verify = cardMemberRepository.findByCardIdAndUserId(card.getId(),member.getId());
        if(verify.isPresent())
            throw new IllegalArgumentException("해당 카드에 해당 작업자가 이미 존재합니다.");

        CardMember cardMember = cardMemberRepository.save(new CardMember(card, member));

        return new CardMemberStatusResponse(201, "CREATED", cardMember.getCard().getId(), cardMember.getUser().getId());
    }

    public List<CardMemberResponse> getCardMember(CardMemberIdRequest cardMemberIdRequest) {
        List<CardMember> cardMemberList = getCardMemberListByCardId(cardMemberIdRequest.getCardId());
        List<CardMemberResponse> cardMemberResponseList = new ArrayList<>();
        for(CardMember cardMember : cardMemberList) {
            cardMemberResponseList.add(new CardMemberResponse(cardMemberIdRequest.getCardId(),cardMember.getUser().getId()));
        }
        return cardMemberResponseList;
    }

    @Transactional
    public CardMemberStatusResponse deleteCardMember(CardMemberRequest cardMemberRequest, User user) {
        Card card = getCardById(cardMemberRequest.getCardId());

        if(!user.getId().equals(card.getOwner().getId()))
            throw new IllegalArgumentException("해당 카드에 작업자를 삭제할 권한이 없습니다.");

        cardMemberRepository.deleteByCardIdAndUserId(cardMemberRequest.getCardId(), cardMemberRequest.getMemberId());

        return new CardMemberStatusResponse(200, "OK", card.getId(),cardMemberRequest.getMemberId());
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(
            ()-> new NullPointerException("해당 유저가 존재하지 않습니다.")
        );
    }

    public List<CardMember> getCardMemberListByCardId(Long cardId){
        return cardMemberRepository.findAllByCardId(cardId);
    }

    public Card getCardById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(
            ()-> new NullPointerException("해당 카드가 존재하지 않습니다.")
        );
    }


}
