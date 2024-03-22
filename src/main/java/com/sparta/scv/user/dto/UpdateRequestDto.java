package com.sparta.scv.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDto {

    private String password;
    private String nickname;
    private String department;
    private String company;
}
