package com.sparta.scv.board.service;

import com.sparta.scv.board.dto.BoardDto;
import com.sparta.scv.board.dto.BoardRequest;
import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.boardmember.entity.BoardMember;
import com.sparta.scv.boardmember.repository.BoardMemberRepository;
import com.sparta.scv.user.entity.User;
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

    @Transactional
    public Long createBoard(User user, BoardRequest boardRequest) {
        Board board = createBoardEntity(user, boardRequest);
        saveBoardAndMember(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public List<BoardDto> getBoards(User user) {
        return boardRepository.getBoards(user);
    }

    @Transactional
    public Long updateBoard(Long boardId, BoardRequest boardRequest, User user) {
        Board board = new Board(boardId);
        boardExistsById(boardId);
        validateBoardOwner(user, board);
        updateBoardAttributes(board, boardRequest);
        return board.getId();
    }

    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = new Board(boardId);
        boardExistsById(boardId);
        validateBoardOwner(user, board);
        board.deleteBoard();
        return board.getId();
    }

    private Board createBoardEntity(User user, BoardRequest boardRequest) {
        return Board.builder()
            .name(boardRequest.getName())
            .description(boardRequest.getDescription())
            .color(boardRequest.getColor())
            .owner(user)
            .build();
    }

    private void saveBoardAndMember(Board board) {
        boardRepository.save(board);
        boardMemberRepository.save(new BoardMember(board.getOwner(), board));
    }


    private void boardExistsById(Long boardId) {
        if(!boardRepository.existsById(boardId)){
            throw new IllegalArgumentException("Board with id " + boardId + " does not exist");
        }
    }

    private void validateBoardOwner(User user, Board board) {
        if (!Objects.equals(board.getOwner().getId(), user.getId())) {
            throw new IllegalArgumentException("Unauthorized to perform action");
        }
    }

    private void updateBoardAttributes(Board board, BoardRequest boardRequest) {
        board.updateBoard(boardRequest.getName(), boardRequest.getDescription(), boardRequest.getColor());
        boardRepository.save(board);
    }
}

