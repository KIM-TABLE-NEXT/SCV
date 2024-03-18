package com.sparta.scv.boardmember;

import com.sparta.scv.boardmember.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

}
