package com.sparta.scv.cardmember.repository;

import com.sparta.scv.cardmember.entity.CardMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardMemberRepository extends JpaRepository<CardMember, Long> {

    Optional<CardMember> findByCardId(Long cardId);

    List<CardMember> findAllByCardId(Long cardId);

    void deleteByCardIdAndUserId(Long cardId, Long memberId);

    Optional<CardMember> findByCardIdAndMemberId(Long id, Long id1);
}
