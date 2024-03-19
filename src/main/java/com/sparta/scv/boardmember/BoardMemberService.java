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
        Board board = getBoard(boardMemberRequest);
        User invitedUser = new User(boardMemberRequest.getUserId());

        checkBoardMemberExists(user, board);

        if (userRepository.existsById(invitedUser.getId())) {
            saveBoardMember(invitedUser, board);
            return invitedUser.getId();
        }

        throw new IllegalArgumentException("Invited user not found");
    }


    @Transactional
    public Long deleteBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = getBoard(boardMemberRequest);

        validateDeletePermission(board, boardMemberRequest.getUserId(), user);

        User deletedUser = new User(boardMemberRequest.getUserId());
        boardMemberRepository.deleteByUserAndBoard(deletedUser, board);
        return deletedUser.getId();
    }

    private Board getBoard(BoardMemberRequest boardMemberRequest) {
        return boardRepository.findById(boardMemberRequest.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Board not found"));
    }

    private void checkBoardMemberExists(User user, Board board) {
        if (!boardMemberRepository.existsByUserAndBoard(user, board)) {
            throw new IllegalArgumentException("User is not a member of this board");
        }
    }

    private void saveBoardMember(User invitedUser, Board board) {
        boardMemberRepository.save(new BoardMember(invitedUser, board));
    }

    private void validateDeletePermission(Board board, Long memberId, User user) {
        if (board.getOwner().getId().equals(memberId)) {
            throw new IllegalArgumentException("Cannot remove board owner");
        }
        if (!board.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Unauthorized to remove board member");
        }
    }
}

