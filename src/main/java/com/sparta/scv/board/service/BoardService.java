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
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public Long createBoard(User user, BoardRequest boardRequest) {
        Board board = createBoardEntity(user, boardRequest);
        saveBoardAndMember(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "board", key = "#user.id", cacheManager = "cacheManager", unless = "#result == null")
    public List<BoardDto> getBoards(User user) {
        return boardRepository.getBoards(user);
    }

    @Transactional
    public Long updateBoard(Long boardId, BoardRequest boardRequest, User user) {

        Board board = getBoardById(boardId);

        RLock lock = redissonClient.getFairLock("board:" + boardId);
        try {
            // tryLock 메소드를 사용하여 10초 동안 락을 획득 시도, 최대 60초 동안 락 유지
            if (lock.tryLock(10, 60, TimeUnit.SECONDS)) {
                try {
                    // 락을 성공적으로 획득한 경우의 처리
                    checkBoardStateIsTrue(board);
                    validateBoardOwner(user, board);
                    updateBoardAttributes(board, boardRequest);
                } finally {
                    lock.unlock();
                }
            } else {
                throw new IllegalArgumentException("다른 유저가 접근중에 있습니다. 다시 시도해 주세요");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return board.getId();
    }

    @Transactional
    public void updateRockBoardTest(Long boardId, BoardRequest boardRequest, User user, int i) {

        Board board = getBoardById(boardId);

        RLock lock = redissonClient.getFairLock("board:" + boardId);
        try {
            lock.lock();
            System.out.println(i + "번째 락 시작 boardId = " + boardId);

            checkBoardStateIsTrue(board);
            validateBoardOwner(user, board);
            updateBoardAttributes(board, boardRequest);
        } finally {
            System.out.println(i + "번째 락 종료 boardId = " + boardId);
            lock.unlock();
        }
    }

    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = getBoardById(boardId);
        validateBoardOwner(user, board);
        board.deleteBoard();
        return board.getId();
    }

    private void checkBoardStateIsTrue(Board board) {
        if (!board.isState()) {
            throw new IllegalArgumentException();
        }
    }

    private Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            IllegalArgumentException::new
        );
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


    private void validateBoardOwner(User user, Board board) {
        if (!Objects.equals(board.getOwner().getId(), user.getId())) {
            throw new IllegalArgumentException("Unauthorized to perform action");
        }
    }

    private void updateBoardAttributes(Board board, BoardRequest boardRequest) {
        board.updateBoard(boardRequest.getName(), boardRequest.getDescription(),
            boardRequest.getColor());
        boardRepository.save(board);
    }
}

