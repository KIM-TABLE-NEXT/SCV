package com.sparta.scv.boardmember;

import com.sparta.scv.board.Board;
import com.sparta.scv.board.BoardRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardMemberService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    public Long addBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = boardRepository.findById(boardMemberRequest.getBoardId()).orElse(null);

        if (!boardMemberRepository.existsByUserAndBoard(user, board)){
            throw new IllegalArgumentException();
        }

        User invitedUser = new User(boardMemberRequest.getUserId());
        if(userRepository.existsById(invitedUser.getId())){
            boardMemberRepository.save(new BoardMember(invitedUser, board));
            return invitedUser.getId();
        }

        throw new IllegalArgumentException();
    }

    @Transactional
    public Long deleteBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = boardRepository.findById(boardMemberRequest.getBoardId()).orElseThrow(
            () -> new IllegalArgumentException("Board not found")
        );
        if (Objects.equals(board.getOwner().getId(), user.getId())) {
            User deletedUser = new User(boardMemberRequest.getUserId());
            boardMemberRepository.deleteByUserAndBoard(deletedUser, board);
            return deletedUser.getId();
        }

        throw new IllegalArgumentException("보드 회원 제거 권한이 없습니다.");
    }
}
