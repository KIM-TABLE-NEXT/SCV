package com.sparta.scv.board;

import com.sparta.scv.board.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUser_id(Long id);

}
