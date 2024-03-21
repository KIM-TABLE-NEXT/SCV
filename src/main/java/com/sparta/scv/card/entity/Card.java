package com.sparta.scv.card.entity;

import com.sparta.scv.boardcolumn.BoardColumn;
import com.sparta.scv.card.dto.request.CardRequest;
import com.sparta.scv.card.dto.request.CardUpdateRequest;
import com.sparta.scv.user.entity.User;
import jakarta.persistence.*;
import com.sparta.scv.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Entity
@Table(name = "cards")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

  @Column(nullable = false)
  private String startDate;

  @Column(nullable = false)
  private String endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "column_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private BoardColumn column;

  @ManyToOne
  @JoinColumn(name="owner_id",nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User owner;

  public Card(CardRequest cardRequest, BoardColumn boardColumn, User user){
    this.title = cardRequest.getTitle();
    this.description = cardRequest.getDescription();
    this.color = cardRequest.getColor();
    this.startDate = cardRequest.getStartDate();
    this.endDate = cardRequest.getEndDate();
    this.column = boardColumn;
    this.owner = user;
  }

  public void update(CardUpdateRequest cardUpdateRequest) {
    this.title = cardUpdateRequest.getTitle();
    this.description = cardUpdateRequest.getDescription();
    this.color = cardUpdateRequest.getColor();
    this.startDate = cardUpdateRequest.getStartDate();
    this.endDate = cardUpdateRequest.getEndDate();
  }

  public void move(BoardColumn boardColumn) {
    this.column = boardColumn;
  }
}
