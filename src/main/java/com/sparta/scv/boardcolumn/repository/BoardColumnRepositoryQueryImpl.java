package com.sparta.scv.boardcolumn.repository;

import static com.sparta.scv.boardcolumn.entity.QBoardColumn.boardColumn;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.scv.boardcolumn.entity.BoardColumn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class BoardColumnRepositoryQueryImpl {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public BoardColumnRepositoryQueryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    public BoardColumn findColumnById(Long boardColumnId) {
        return qf
            .selectFrom(boardColumn)
            .leftJoin(boardColumn.board).fetchJoin()
            .leftJoin(boardColumn.board.owner).fetchJoin()
            .where(boardColumn.id.eq(boardColumnId))
            .fetchOne();
    }

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
