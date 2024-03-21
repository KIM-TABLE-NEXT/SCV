package com.sparta.scv.boardmember.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class BoardMemberId implements Serializable {

    private Long userId;
    private Long boardId;
}
