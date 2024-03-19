package com.sparta.scv.board;

import com.sparta.scv.boardmember.BoardMember;
import com.sparta.scv.boardmember.BoardMemberRepository;
import com.sparta.scv.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<BoardDto> getBoards(User user) {
        return boardRepository.getBoards(user);
    }


    public Long updateBoard(Long boardId, BoardRequest boardRequest, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            IllegalArgumentException::new
        );

        if(Objects.equals(board.getOwner().getId(), user.getId())) {
            board.updateBoard(
                boardRequest.getName(),
                boardRequest.getDescription(),
                boardRequest.getColor()
            );
            boardRepository.save(board);
            return board.getId();
        }

        throw new IllegalArgumentException();
    }


    public Long deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            IllegalArgumentException::new
        );

        if(Objects.equals(board.getOwner().getId(), user.getId())) {
            board.deleteBoard();
            boardRepository.save(board);
            return board.getId();
        }

        throw new IllegalArgumentException();
    }

}
