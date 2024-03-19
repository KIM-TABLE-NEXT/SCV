package com.sparta.scv.board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.scv.boardmember.QBoardMember;
import com.sparta.scv.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class BoardRepositoryQueryImpl implements BoardRepositoryQuery{

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> getBoards(User user) {
        QBoard qBoard = QBoard.board;
        QBoardMember qBoardMember = QBoardMember.boardMember;

        return queryFactory
            .select(Projections.constructor(BoardDto.class,
                qBoard.id,
                qBoard.name))
            .from(qBoardMember)
            .join(qBoardMember.board, qBoard)
            .where(qBoardMember.user.eq(user)
                .and(qBoard.state.isTrue()))
            .fetch();
    }
}

