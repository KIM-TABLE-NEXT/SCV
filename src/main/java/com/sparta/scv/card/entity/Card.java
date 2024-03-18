package com.sparta.scv.card.entity;

import com.sparta.scv.boardcolumn.BoardColumn;
import com.sparta.scv.card.dto.request.CardRequest;
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

  public Card(String title, String description, String color, BoardColumn boardColumn, User user){
    this.title = title;
    this.description = description;
    this.color = color;
    this.column = boardColumn;
    this.owner = user;
  }

  public void update(CardRequest cardRequest, BoardColumn boardColumn) {
    this.title = cardRequest.getTitle();
    this.description = cardRequest.getDescription();
    this.color = cardRequest.getColor();
    this.column = boardColumn;
  }
}
