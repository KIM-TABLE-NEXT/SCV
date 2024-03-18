package com.sparta.scv.card;

import com.sparta.scv.boardcolumn.BoardColumn;
import com.sparta.scv.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Entity
@Table(name = "cards")
@Getter
@NoArgsConstructor
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "column_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private BoardColumn column;

  @ManyToOne
  @JoinColumn(name="owner_id",nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User owner;

  public void updateColumn(BoardColumn column){
    this.column = column;
  }
}
