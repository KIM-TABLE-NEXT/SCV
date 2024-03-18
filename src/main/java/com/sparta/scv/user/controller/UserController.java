package com.sparta.scv.user.controller;

import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/users/signup")
  public ResponseEntity<SignupDto> signup(@RequestBody SignupDto requestDto) {
    return  ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.signup(requestDto));
  }
  @PostMapping("/users/login")
  public ResponseEntity<Long> signup(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse res) {
    return  ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.login(requestDto,res));
  }

}
