package com.sparta.scv.boardcolumn;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import static com.sparta.scv.boardcolumn.QBoardColumn.boardColumn;


@Repository
public class BoardColumnRepositoryQueryImpl {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public BoardColumnRepositoryQueryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

//    public Long findNextColumnByPosition(Long boardId, Long index) {
//        var query = qf
//            .select(boardColumn.position)
//            .from(boardColumn)
//            .where(boardColumn.board.id.eq(boardId)
//                .and(boardColumn.position.gt(position)))
//            .orderBy(boardColumn.position.asc())
//            .limit(1);
//
//        return query.fetchFirst();
//    }

    public Long findColumnByPosition(Long boardId, Long index) {
        Long result = qf
            .select(boardColumn.position)
            .from(boardColumn)
            .where(boardColumn.board.id.eq(boardId))
            .orderBy(boardColumn.position.asc())
            .offset(index - 1)
            .limit(1)
            .fetchOne();
        return result != null ? result : 0L;
    }
}
