package com.sparta.scv.user.controller;

import com.sparta.scv.global.impl.UserDetailsImpl;
import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User 관리", description = "User 관리에 관련된 API")
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 진행한다.")
    public ResponseEntity<String> SignUp(@RequestBody SignupDto requestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signup(requestDto));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 진행한 후 JWT 토큰을 발급한다.")
    public ResponseEntity<Long> Login(@RequestBody LoginRequestDto requestDto,
        HttpServletResponse res) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(requestDto, res));
    }

    @PutMapping("/update-profile")
    @Operation(summary = "유저 정보 수정", description = "유저 정보를 변경한다.")
    public ResponseEntity<Long> updateProfile(@RequestBody UpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl impl) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.update(requestDto, impl.getUser()));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "유저 삭제", description = "유저를 삭제한다.")
    public ResponseEntity<Long> deleteAccount(
        @AuthenticationPrincipal UserDetailsImpl impl) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.delete(impl.getUser()));
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행한 후 JWT 토큰을 제거한다.")
    public ResponseEntity<?> logout(
        HttpServletResponse servletResponse) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.userLogout(servletResponse));
    }

}
