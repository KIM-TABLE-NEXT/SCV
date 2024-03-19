package com.sparta.scv.boardcolumn;

import com.sparta.scv.board.Board;
import com.sparta.scv.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnRepositoryQueryImpl boardColumnRepositoryQuery;
    private final BoardRepository boardRepository;

    public List<BoardColumnResponseDto> getColumns(GetColumnsRequestDto requestDto) {
        return boardColumnRepository.findByBoardIdOrderByPositionAsc(requestDto.getBoardId())
            .stream().map(BoardColumnResponseDto::new).toList();
    }

    @Transactional
    public Long createColumn(BoardColumnRequestDto requestDto) {
        validatePosition(requestDto.getPosition());

        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 보드는 존재하지 않습니다.")
        );

        Long position = calculatePosition(requestDto.getBoardId(), requestDto.getPosition());
        BoardColumn savedBoardColumn = boardColumnRepository.save(new BoardColumn(requestDto.getColumnName(), position, board));

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
        validatePosition(requestDto.getPosition());

        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다."));

        Long position = calculatePosition(boardColumn.getBoard().getId(), requestDto.getPosition());
        boardColumn.updatePosition(position);
    }

    @Transactional
    public void deleteColumn(Long boardColumnId) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다.")
        );
        boardColumnRepository.delete(boardColumn);
    }

    private void validatePosition(Long position) {
        if (position < 1) {
            throw new IllegalArgumentException("순서 값은 1 이상의 값이여야 합니다.");
        }
    }

    private Long calculatePosition(Long boardId, Long requestedPosition) {
        Long maxPosition = boardColumnRepository.findMaxPosition(boardId).orElse(0L) / 1024;
        if (requestedPosition > maxPosition + 1) {
            requestedPosition = maxPosition + 2;
        }

        Long nextPosition = boardColumnRepositoryQuery.findColumnByPosition(boardId, requestedPosition);
        Long previousPosition = (requestedPosition == 1) ? 0 :
            boardColumnRepositoryQuery.findColumnByPosition(boardId, requestedPosition - 1);

        if (nextPosition == 0) {
            return (maxPosition + 1) * 1024;
        } else if (previousPosition == 0) {
            return nextPosition / 2;
        } else {
            return (nextPosition + previousPosition) / 2;
        }
    }
}
