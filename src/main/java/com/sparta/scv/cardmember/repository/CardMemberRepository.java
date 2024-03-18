package com.sparta.scv.cardmember.repository;

import com.sparta.scv.cardmember.entity.CardMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardMemberRepository extends JpaRepository<CardMember, Long> {

}
