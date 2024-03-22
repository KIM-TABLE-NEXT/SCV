package com.sparta.scv.user.entity;

import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@DynamicUpdate
@AllArgsConstructor

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

    public User(SignupDto signupDto) {
        this.username = signupDto.getUsername();
        this.company = signupDto.getCompany();
        this.department = signupDto.getDepartment();
        this.nickname = signupDto.getNickname();
        this.password = signupDto.getPassword();
    }

    public User(String username, String company, String department, String nickname,
        String password) {
        this.username = username;
        this.company = company;
        this.department = department;
        this.nickname = nickname;
        this.password = password;
    }

    public User(Long id) {
        this.id = id;
    }

    @Transactional
    public void update(UpdateRequestDto dto) {
      if (dto.getCompany() != null) {
        this.company = dto.getCompany();
      }
      if (dto.getDepartment() != null) {
        this.department = dto.getDepartment();
      }
      if (dto.getNickname() != null) {
        this.nickname = dto.getNickname();
      }
      if (dto.getPassword() != null) {
        this.password = dto.getPassword();
      }
    }
}
