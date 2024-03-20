package com.sparta.scv.boardmember.repository;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.boardmember.entity.BoardMember;
import com.sparta.scv.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

    List<BoardMember> findBoardsByUser(User user);

    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);

    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
}
