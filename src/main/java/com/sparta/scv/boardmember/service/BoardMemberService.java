package com.sparta.scv.boardmember.service;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.boardmember.dto.BoardMemberRequest;
import com.sparta.scv.boardmember.dto.BoardMembersRequest;
import com.sparta.scv.boardmember.entity.BoardMember;
import com.sparta.scv.boardmember.repository.BoardMemberRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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
        Board board = new Board(boardMemberRequest.getBoardId());
        User invitedUser = new User(boardMemberRequest.getUserId());

        existsBoardById(boardMemberRequest.getBoardId());
        existsBoardMemberByUserAndBoard(user, board);
        existsUserById(invitedUser);

        boardMemberRepository.save(new BoardMember(invitedUser, board));

        return invitedUser.getId();
    }



    public String addBoardMembers(Long boardId, User user, BoardMembersRequest boardMembersRequest) {
        Board board = new Board(boardId);

        existsBoardById(boardId);
        existsBoardMemberByUserAndBoard(user, board);

        List<String> newMembers = boardMembersRequest.getBoardMembersName();

        List<User> existingUsers = userRepository.findAllByUsernameIn(newMembers);

        List<BoardMember> boardMemberList = existingUsers.stream()
            .map(u -> new BoardMember(u, board))
            .collect(Collectors.toList());

        // 보드 멤버를 저장합니다.
        boardMemberRepository.saveAll(boardMemberList);

        return "success";
    }


    @Transactional
    public Long deleteBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = new Board(boardMemberRequest.getBoardId());
        User deletedUser = new User(boardMemberRequest.getUserId());

        existsBoardById(boardMemberRequest.getBoardId());
        validateDeletePermission(board, boardMemberRequest.getUserId(), user);
        boardMemberRepository.deleteByUserAndBoard(deletedUser, board);

        return deletedUser.getId();
    }

    private void existsUserById(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }
    }

    private void existsBoardById(Long boardId) {
        if(!boardRepository.existsById(boardId)) {
            throw new IllegalArgumentException("Board not found");
        }
    }

    private void existsBoardMemberByUserAndBoard(User user, Board board) {
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

