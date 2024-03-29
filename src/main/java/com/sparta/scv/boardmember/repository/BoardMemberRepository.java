package com.sparta.scv.boardmember.repository;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.boardmember.entity.BoardMember;
import com.sparta.scv.user.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {


    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);

    @Cacheable(value = "boardMembers", key = "#userId + '_' + #boardId")
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
}
