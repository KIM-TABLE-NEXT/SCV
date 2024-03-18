package com.sparta.scv.user.entity;

import com.sparta.scv.user.dto.SignupDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String nickname;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private String company;

  public User(SignupDto signupDto){
    this.username = signupDto.getUsername();
    this.company = signupDto.getCompany();
    this.department = signupDto.getDepartment();
    this.nickname = signupDto.getNickname();
    this.password = signupDto.getPassword();
  }
}
