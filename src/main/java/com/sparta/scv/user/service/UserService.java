package com.sparta.scv.user.service;

import com.sparta.scv.global.jwt.JwtUtil;
import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserNamePassword;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    String token;
    try {
       user = userRepository.findByUsernameAndPassword(username,password);
       token = jwtUtil.createToken(user.getId());
    }catch (Exception e){
      throw new NoSuchElementException("유저의 아이디 혹은 비밀 번호가 틀렸습니다.");
    }
    jwtUtil.addJwtToHeader(token,httpServletResponse);
    return jwtUtil.giveUserId(token);
  }

  //
  @Transactional
  public Long update(UpdateRequestDto requestDto, User user) throws NoSuchElementException {
    User updateuser = user;
    updateuser.update(requestDto);
    return updateuser.getId();
  }

  @Transactional
  public Long delete(User user) throws NoSuchElementException {
    long id = user.getId();
    try {
      userRepository.delete(user);
    }catch (Exception e){
      throw new NoSuchElementException("해당 유저를 지우는데 실패");
    }
    return id;
  }
}
