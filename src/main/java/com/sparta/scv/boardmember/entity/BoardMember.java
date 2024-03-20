package com.sparta.scv.boardmember.entity;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.user.entity.User;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boardmembers")
@NoArgsConstructor
@Getter
public class BoardMember {

  @EmbeddedId
  private BoardMemberId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User user;


  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("boardId")
  @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Board board;

  public BoardMember(User user, Board board) {
    this.id = new BoardMemberId();
    this.id.setUserId(user.getId());
    this.id.setBoardId(board.getId());

    this.user = user;
    this.board = board;
  }

}
