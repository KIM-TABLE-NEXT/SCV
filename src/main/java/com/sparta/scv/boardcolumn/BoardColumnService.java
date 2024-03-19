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
    private final BoardColumnRepositoryQueryImpl boardColumnRepositoryQuery;

    public List<BoardColumnResponseDto> getColumns(GetColumnsRequestDto requestDto) {
        return boardColumnRepository.findByBoardIdOrderByPositionAsc(requestDto.getBoardId())
            .stream().map(BoardColumnResponseDto::new).toList();
    }

    @Transactional
    public Long createColumn(BoardColumnRequestDto requestDto) {
        Long originPosition = requestDto.getPosition();
        if (originPosition < 1) {
            throw new IllegalArgumentException("순서 값은 1 이상의 값이여야 합니다.");
        }
        Long maxPosition = boardColumnRepository.findMaxPosition(requestDto.getBoardId()).orElse(0L) / 1024;
        if (originPosition > maxPosition + 1) {
            originPosition = maxPosition + 2;
        }
        Board board = new Board();
        board.setId(requestDto.getBoardId());
        Long gapPosition = originPosition * 1024L - 1024L;
        Long foundPosition = boardColumnRepositoryQuery.findColumnByPosition(requestDto.getBoardId(), gapPosition);
        if(foundPosition != null){
            Long previousPosition = boardColumnRepositoryQuery.findPreviousColumnByPosition(requestDto.getBoardId(), gapPosition);
            Long setPosition = (foundPosition - previousPosition) / 2;
            BoardColumn savedBoardColumn = boardColumnRepository.save(new BoardColumn(requestDto.getColumnName(), setPosition, board));
            return savedBoardColumn.getId();
        }
        if(gapPosition == 0){
            gapPosition = 1024L / 2L;
        }
        BoardColumn savedBoardColumn = boardColumnRepository.save(new BoardColumn(requestDto.getColumnName(), gapPosition, board));
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
        Long originPosition = requestDto.getPosition();
        if (originPosition < 1) {
            throw new IllegalArgumentException("순서 값은 1 이상의 값이여야 합니다.");
        }
        Long maxPosition = boardColumnRepository.findMaxPosition(boardColumn.getBoard().getId()).orElse(0L) / 1024;
        if (originPosition > maxPosition + 1) {
            originPosition = maxPosition + 2;
        }
        Long gapPosition = originPosition * 1024L - 1024L;
        Long foundPosition = boardColumnRepositoryQuery.findColumnByPosition(boardColumn.getBoard().getId(), gapPosition);

        if(foundPosition != null){
            Long previousPosition = boardColumnRepositoryQuery.findPreviousColumnByPosition(boardColumn.getBoard().getId(), gapPosition);
            Long setPosition = (foundPosition - previousPosition) / 2;
            boardColumn.updatePosition(setPosition);
            return;
        }
        boardColumn.updatePosition(gapPosition);
    }

    @Transactional
    public void deleteColumn(Long boardColumnId) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId).orElseThrow(
            () -> new IllegalArgumentException("해당 ID를 가진 컬럼은 존재하지 않습니다.")
        );
        boardColumnRepository.delete(boardColumn);
    }
}
