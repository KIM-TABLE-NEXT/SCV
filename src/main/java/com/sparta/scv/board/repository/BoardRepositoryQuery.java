package com.sparta.scv.board.repository;

import com.sparta.scv.board.dto.BoardDto;
import com.sparta.scv.user.entity.User;
import java.util.List;

public interface BoardRepositoryQuery {
    List<BoardDto> getBoards(User user);
}
