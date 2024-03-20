package com.sparta.scv.boardcolumn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    BoardColumn findByPosition(Long originPosition);

    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);

    List<BoardColumn> findByBoardIdAndPositionBetween(Long boardId, Long oldPosition, Long newPosition);

    @Query("SELECT MAX(c.position) FROM BoardColumn c WHERE c.board.id = :boardId")
    Optional<Long> findMaxPosition(Long boardId);

    List<BoardColumn> findByBoardIdAndPositionGreaterThan(Long boardId, Long position);
}
