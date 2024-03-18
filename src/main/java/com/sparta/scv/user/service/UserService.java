package com.sparta.scv.user.service;

import com.sparta.scv.global.jwt.JwtUtil;
import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserNamePassword;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final String Auth = "Authorization";
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public SignupDto signup(SignupDto requestDto) {
    User user = new User(requestDto);
    try {
      userRepository.save(user);
    }catch (DuplicateKeyException e){
      throw new IllegalArgumentException("해당 유저는 이미 존재 합니다");
    }
    return requestDto;
  }

  public Long login(LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
    String username = requestDto.getUsername();
    String password = requestDto.getPassword();
    UserNamePassword user;
    try {
       user = userRepository.findByUsernameAndPassword(username,password);
    }catch (NoSuchElementException e){
      throw new NoSuchElementException("유저의 아이디 혹은 비밀 번호가 틀렸습니다.");
    }
    String token = jwtUtil.createToken(user.getId());
    jwtUtil.addJwtToHeader(token,httpServletResponse);
    return jwtUtil.giveUserId(token);
  }
}
