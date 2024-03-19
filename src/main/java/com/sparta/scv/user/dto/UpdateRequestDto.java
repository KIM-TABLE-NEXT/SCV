package com.sparta.scv.user.dto;

import lombok.Getter;

@Getter
public class UpdateRequestDto {
  private String password;
  private String nickname;
  private String department;
  private String company;
}
