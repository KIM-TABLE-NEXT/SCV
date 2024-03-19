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

  @ManyToOne
  @JoinColumn(name="owner_id",nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User owner;



}
