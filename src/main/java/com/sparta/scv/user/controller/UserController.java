package com.sparta.scv.user.controller;

import com.sparta.scv.global.impl.UserDetailsImpl;
import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/users/signup")
  public ResponseEntity<SignupDto> SignUp(@RequestBody SignupDto requestDto) {
    return  ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.signup(requestDto));
  }
  @PostMapping("/users/login")
  public ResponseEntity<Long> Login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse res) {
    return  ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.login(requestDto,res));
  }
  @PutMapping("/users/updateprofile")
  public ResponseEntity<Long> updateProfile(@RequestBody UpdateRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl impl){
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.update(requestDto,impl.getUser()));
  }
  @DeleteMapping("/users/delete")
  public ResponseEntity<Long> deleteAccount(
      @AuthenticationPrincipal UserDetailsImpl impl){
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.delete(impl.getUser()));
  }
  @GetMapping("/users/logout")
  public ResponseEntity<?> logout(
      HttpServletResponse servletResponse){
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.userLogout(servletResponse));
  }

}
