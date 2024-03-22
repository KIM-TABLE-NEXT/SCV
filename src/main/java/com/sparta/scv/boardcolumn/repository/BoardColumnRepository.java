package com.sparta.scv.boardcolumn.repository;

import com.sparta.scv.boardcolumn.entity.BoardColumn;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    BoardColumn findByPosition(Long originPosition);

    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);

    @Query("SELECT MAX(c.position) FROM BoardColumn c WHERE c.board.id = :boardId")
    Optional<Long> findMaxPosition(Long boardId);

    List<BoardColumn> findByBoardIdAndPositionGreaterThan(Long boardId, Long position);
}
