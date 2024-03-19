package com.sparta.scv.boardmember;

import com.sparta.scv.board.Board;
import com.sparta.scv.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

    List<BoardMember> findBoardsByUser(User user);

    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);
}
