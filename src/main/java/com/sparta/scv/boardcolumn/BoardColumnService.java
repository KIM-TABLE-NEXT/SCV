package com.sparta.scv.boardcolumn;

import com.sparta.scv.annotation.WithDistributedLock;
import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnRepositoryQueryImpl boardColumnRepositoryQuery;
    private final BoardRepository boardRepository;
    private final RedissonClient redissonClient;

    @Cacheable(value = "columns", key = "#requestDto.boardId", cacheManager = "cacheManager")
    public Columns getColumns(BoardIdRequestDto requestDto) {
        List<BoardColumnResponseDto> boardColumns = boardColumnRepository.findByBoardIdOrderByPositionAsc(requestDto.getBoardId())
            .stream().map(BoardColumnResponseDto::new).toList();
        return new Columns(boardColumns);
    }

    @Transactional
    @CacheEvict(value = "columns", key = "#requestDto.boardId", allEntries = true)
    public Long createColumn(BoardColumnRequestDto requestDto) {
        validatePosition(requestDto.getPosition());

        Board board = findBoard(requestDto.getBoardId());

        Long position = calculatePosition(requestDto.getBoardId(), requestDto.getPosition());
        BoardColumn savedBoardColumn = boardColumnRepository.save(new BoardColumn(requestDto.getColumnName(), position, board));

        return savedBoardColumn.getId();
    }

    @Transactional
    @WithDistributedLock(lockName = "#boardColumnId")
    @CacheEvict(value = "columns", key = "#requestDto.boardId", allEntries = true)
    public void updateColumnName(Long boardColumnId, NameUpdateDto requestDto) {
        String lockKey = "Column" + boardColumnId;
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    BoardColumn boardColumn = findColumn(boardColumnId);
                    boardColumn.updateName(requestDto.getBoardColumnName());
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Transactional
    public void updateRockColumnTest(Long boardColumnId, String columnName, int i) {

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
        String lockKey = "Column" + boardColumnId;
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    validatePosition(requestDto.getPosition());

                    BoardColumn boardColumn = findColumn(boardColumnId);

                    Long position = calculatePosition(boardColumn.getBoard().getId(), requestDto.getPosition());
                    boardColumn.updatePosition(position);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Transactional
    @CacheEvict(value = "columns", key = "#boardId", allEntries = true)
    public void deleteColumn(Long boardId, Long boardColumnId, User user) {
        BoardColumn boardColumn = findColumn(boardColumnId);
        if (!Objects.equals(boardColumn.getBoard().getOwner().getId(), user.getId())) {
            throw new IllegalArgumentException("컬럼의 삭제는 보드의 주인만 가능합니다.");
        };
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

    private Long calculatePosition(Long boardId, Long requestedPosition) {
        Long maxPosition = boardColumnRepository.findMaxPosition(boardId).orElse(0L) / 1024;
        if (requestedPosition > maxPosition + 1) { // 컬럼의 갯수보다 큰 포지션(순서)이 입력되었을 경우 값 조정
            requestedPosition = maxPosition + 2;
        }

        Long nextPosition = boardColumnRepositoryQuery.findColumnByPosition(boardId, requestedPosition);
        Long previousPosition = (requestedPosition == 1) ? 0 : // 이전 포지션의 컬럼이 없다면 0으로 설정
            boardColumnRepositoryQuery.findColumnByPosition(boardId, requestedPosition - 1);

        if (nextPosition == 0) { // DB에 컬럼이 존재하지 않는 경우
            return (maxPosition + 1) * 1024;
        } else if (previousPosition == 0) { // 이전 순서의 컬럼이 존재하지 않는 경우 (입력된 순서가 1)
            return nextPosition / 2;
        } else { // 두 포지션의 중간값 계산
            return (nextPosition + previousPosition) / 2;
        }
    }
}
