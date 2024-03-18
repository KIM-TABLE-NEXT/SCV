package com.sparta.scv.board;

import com.sparta.scv.boardmember.BoardMember;
import com.sparta.scv.boardmember.BoardMemberRepository;
import com.sparta.scv.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    public Long createBoard(User user, BoardRequest boardRequest) {
        Board board = Board.builder()
            .name(boardRequest.getName())
            .description(boardRequest.getDescription())
            .color(boardRequest.getColor())
            .owner(user)
            .build();

        boardRepository.save(board);
        boardMemberRepository.save(new BoardMember(user, board));
        return board.getId();
    }
}
