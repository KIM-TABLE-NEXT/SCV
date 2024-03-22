package com.sparta.scv.boardcolumn.service;

import com.sparta.scv.annotation.WithDistributedLock;
import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.boardcolumn.dto.BoardColumnRequestDto;
import com.sparta.scv.boardcolumn.dto.BoardColumnResponseDto;
import com.sparta.scv.boardcolumn.dto.BoardIdRequestDto;
import com.sparta.scv.boardcolumn.dto.Columns;
import com.sparta.scv.boardcolumn.dto.NameUpdateDto;
import com.sparta.scv.boardcolumn.dto.PositionUpdateDto;
import com.sparta.scv.boardcolumn.entity.BoardColumn;
import com.sparta.scv.boardcolumn.repository.BoardColumnRepository;
import com.sparta.scv.boardcolumn.repository.BoardColumnRepositoryQueryImpl;
import com.sparta.scv.user.entity.User;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnRepositoryQueryImpl boardColumnRepositoryQuery;
    private final BoardRepository boardRepository;
    private final RedissonClient redissonClient;

    @Cacheable(value = "columns", key = "#requestDto.boardId", cacheManager = "cacheManager")
    public Columns getColumns(BoardIdRequestDto requestDto) {
        List<BoardColumnResponseDto> boardColumns = boardColumnRepository.findByBoardIdOrderByPositionAsc(
                requestDto.getBoardId())
            .stream().map(BoardColumnResponseDto::new).toList();
        return new Columns(boardColumns);
    }

    @Transactional
    @CacheEvict(value = "columns", key = "#requestDto.boardId", allEntries = true)
    public Long createColumn(BoardColumnRequestDto requestDto) {
        validatePosition(requestDto.getPosition());

        Board board = findBoard(requestDto.getBoardId());

        Long position = calculatePosition(requestDto.getBoardId(), requestDto.getPosition(), 0);
        BoardColumn savedBoardColumn = boardColumnRepository.save(
            new BoardColumn(requestDto.getColumnName(), position, board));

        return savedBoardColumn.getId();
    }

    @Transactional
    @WithDistributedLock(lockName = "#boardColumnId")
    @CacheEvict(value = "columns", key = "#requestDto.boardId", allEntries = true)
    public void updateColumnName(Long boardColumnId, NameUpdateDto requestDto) {
        BoardColumn boardColumn = findColumn(boardColumnId);
        boardColumn.updateName(requestDto.getBoardColumnName());
    }

    @Transactional
    public void updateLockColumnTest(Long boardColumnId, String columnName, int i) {

        BoardColumn boardColumn = findColumn(boardColumnId);

        RLock lock = redissonClient.getFairLock("board:" + boardColumnId);
        try {
            lock.lock();
            System.out.println(i + "번째 락 시작 boardId = " + boardColumnId);

            boardColumn.updateName(columnName);
        } finally {
            System.out.println(i + "번째 락 종료 boardId = " + boardColumnId);
            lock.unlock();
        }
    }


    @Transactional
    @WithDistributedLock(lockName = "#boardColumnId")
    @CacheEvict(value = "columns", key = "#requestDto.boardId", allEntries = true)
    public void updateColumnPosition(Long boardColumnId, PositionUpdateDto requestDto) {
        validatePosition(requestDto.getPosition());

        BoardColumn boardColumn = findColumn(boardColumnId);

        Long position = calculatePosition(boardColumn.getBoard().getId(), requestDto.getPosition(), 1);
        boardColumn.updatePosition(position);
    }

    @Transactional
    @CacheEvict(value = "columns", key = "#boardId", allEntries = true)
    public void deleteColumn(Long boardId, Long boardColumnId, User user) {
        BoardColumn boardColumn = findColumn(boardColumnId);
        if (!Objects.equals(boardColumn.getBoard().getOwner().getId(), user.getId())) {
            throw new IllegalArgumentException("컬럼의 삭제는 보드의 주인만 가능합니다.");
        }
        ;
        boardColumnRepository.delete(boardColumn);
    }

    private BoardColumn findColumn(Long boardColumnId) {
        return boardColumnRepositoryQuery.findColumnById(boardColumnId);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 보드는 존재하지 않습니다.")
        );
    }

    private void validatePosition(Long position) {
        if (position < 1) {
            throw new IllegalArgumentException("순서 값은 1 이상의 값이여야 합니다.");
        }
    }

    private Long calculatePosition(Long boardId, Long requestedPosition, int index) {
        Long columnQuantity = boardColumnRepositoryQuery.findQuantityByBoardId(boardId);
        if (requestedPosition > columnQuantity + 1) {
            requestedPosition = columnQuantity + 1;
        }

        Long nextPosition = boardColumnRepositoryQuery.findColumnByPosition(boardId,
            requestedPosition + index);
        if (nextPosition == 0) { // DB에 해당 순서의 컬럼이 존재하지 않는 경우
            return columnQuantity * 1024;
        } else if (requestedPosition.equals(columnQuantity)) { // 컬럼의 순서를 맨 뒤로 이동하는 경우
            return (nextPosition + 1024) / 2;
        }

        Long previousPosition = (requestedPosition == 1) ? 0 : // 이전 포지션의 컬럼이 없다면 0으로 설정
            boardColumnRepositoryQuery.findColumnByPosition(boardId, requestedPosition - 1 + index);
        if (previousPosition == 0) { // 이전 순서의 컬럼이 존재하지 않는 경우 (입력된 순서가 1)
            return nextPosition / 2;
        } else { // 두 포지션의 중간값 계산
            return (nextPosition + previousPosition) / 2;
        }
    }
}