package com.sparta.scv.board;

import com.sparta.scv.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boards")
@NoArgsConstructor
@Getter
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "board_name")
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  private boolean state;

  @ManyToOne
  @JoinColumn(name="owner_id",nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User owner;

  @Builder
  public Board(String name, String description, String color, User owner) {
    this.name = name;
    this.description = description;
    this.color = color;
    this.state = true;
    this.owner = owner;
  }

  public void updateBoard(String name, String description, String color) {
    this.name = name;
    this.description = description;
    this.color = color;
  }

  public void deleteBoard() {
    this.state = false;
  }
}
