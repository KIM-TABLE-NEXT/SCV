package com.sparta.scv.board;

import com.sparta.scv.user.entity.User;
import java.util.List;

public interface BoardRepositoryQuery {
    List<BoardDto> getBoards(User user);
}
