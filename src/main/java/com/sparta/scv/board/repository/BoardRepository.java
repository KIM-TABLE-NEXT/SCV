package com.sparta.scv.board.repository;

import com.sparta.scv.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryQuery {


}
