package com.sparta.scv.boardcolumn;

import com.sparta.scv.board.BoardIdHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NameUpdateDto implements BoardIdHolder {

    private Long boardId;
    private String boardColumnName;

    @Override
    public Long getBoardId() {
        return boardId;
    }
}
