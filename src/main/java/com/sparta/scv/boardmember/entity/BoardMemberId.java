package com.sparta.scv.boardmember.entity;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.user.entity.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class BoardMemberId implements Serializable {

    private Long userId;
    private Long boardId;
}
