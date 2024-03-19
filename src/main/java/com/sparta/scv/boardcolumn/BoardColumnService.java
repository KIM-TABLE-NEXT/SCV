package com.sparta.scv.boardcolumn;

import com.sparta.scv.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;

    @Transactional(readOnly = true)
    public List<BoardColumnResponseDto> getColumns(GetColumnsRequestDto requestDto) {
        return boardColumnRepository.findByBoardIdOrderByPositionAsc(requestDto.getBoardId())
            .stream().map(BoardColumnResponseDto::new).toList();
    }

    @Transactional
    public Long createColumn(BoardColumnRequestDto requestDto) {
        BoardColumn originPositionColumn = boardColumnRepository.findByPosition(requestDto.getPosition());
        if (originPositionColumn != null) {
            createPosition(requestDto.getBoardId(), requestDto.getPosition());
        }

        Long maxPosition = boardColumnRepository.findMaxPosition(requestDto.getBoardId()).orElse(0L);
        if (requestDto.getPosition() < 1 || requestDto.getPosition() > maxPosition + 1) {
            throw new IllegalArgumentException("입력된 순서값이 올바르지 않습니다.");
        }
        Board board = new Board();
        board.setId(requestDto.getBoardId());
        BoardColumn savedBoardColumn = boardColumnRepository.save(new BoardColumn(requestDto.getColumnName(), requestDto.getPosition(), board));
        return savedBoardColumn.getId();
    }

    @Transactional
    public void updateColumnName(Long boardColumnId, NameUpdateDto requestDto) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다.")
        );
        boardColumn.updateName(requestDto.getBoardColumnName());
    }

    @Transactional
    public void updateColumnPosition(Long boardColumnId, PositionUpdateDto requestDto) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다.")
        );
        Long oldPosition = boardColumn.getPosition();
        Long newPosition = requestDto.getPosition();
        updatePosition(boardColumn.getBoard().getId(), oldPosition, newPosition);
        boardColumn.updatePosition(requestDto.getPosition());
    }

    @Transactional
    public void deleteColumn(Long boardColumnId) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다.")
        );
        boardColumnRepository.delete(boardColumn);
    }

    private void createPosition(Long boardId, Long position) {
        List<BoardColumn> affectedColumns = boardColumnRepository.findByBoardIdAndPositionGreaterThan(boardId, position - 1);
        affectedColumns.forEach(c -> c.setPosition(c.getPosition() + 1));
    }

    private void updatePosition(Long boardId, Long oldPosition, Long newPosition) {
        if (newPosition > oldPosition) {
            List<BoardColumn> affectedColumns =
                boardColumnRepository.findByBoardIdAndPositionBetween(boardId, oldPosition + 1L, newPosition);
            affectedColumns.forEach(c -> c.setPosition(c.getPosition() - 1));
        } else if (newPosition < oldPosition) {
            List<BoardColumn> affectedColumns =
                boardColumnRepository.findByBoardIdAndPositionBetween(boardId, newPosition, oldPosition - 1L);
            affectedColumns.forEach(c -> c.setPosition(c.getPosition() + 1));
        }
    }
}
