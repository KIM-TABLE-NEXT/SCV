package com.sparta.scv.boardmember.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final JPAQueryFactory queryFactory;

    public Long addBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = getBoard(boardMemberRequest.getBoardId());
        User invitedUser = new User(boardMemberRequest.getUserId());

        checkBoardMemberExists(user, board);

        if (userRepository.existsById(invitedUser.getId())) {
            saveBoardMember(invitedUser, board);
            return invitedUser.getId();
        }

        throw new IllegalArgumentException("Invited user not found");
    }

    public String addBoardMembers(Long boardId, User user, BoardMembersRequest boardMembersRequest) {
        Board board = getBoard(boardId);
        /*
        1. 보드 생성
        2. 초대하는 user 권한 검증
        3. 초대받는 user 존재하는 user인지 검증
        4. 초대받은 user가 이미 BoardMember에 속해있는지 검증
        5. 위 검증에서 걸리지 않은 사람은 saveAll
         */

        // 새로 추가할 보드 멤버들의 사용자 이름 목록을 가져옵니다.
        List<String> newMembers = boardMembersRequest.getBoardMembersName();

        // 실존하는 유저인지 인증
        List<User> existingUsers = userRepository.findAllByUsernameIn(newMembers);

        // 실존하는 유저를 보드멤버에 추가
        List<BoardMember> boardMemberList = existingUsers.stream()
            .map(u -> new BoardMember(u, board))
            .collect(Collectors.toList());

        // 보드 멤버를 저장합니다.
        boardMemberRepository.saveAll(boardMemberList);

        return "success";
    }


    @Transactional
    public Long deleteBoardMember(BoardMemberRequest boardMemberRequest, User user) {
        Board board = getBoard(boardMemberRequest.getBoardId());

        validateDeletePermission(board, boardMemberRequest.getUserId(), user);

        User deletedUser = new User(boardMemberRequest.getUserId());
        boardMemberRepository.deleteByUserAndBoard(deletedUser, board);
        return deletedUser.getId();
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
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

