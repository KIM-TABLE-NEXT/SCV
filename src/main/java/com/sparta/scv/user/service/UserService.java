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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final String Auth = "Authorization";
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public String signup(SignupDto requestDto) {
    requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
    User user = new User(requestDto);
    try {
      userRepository.save(user);
      return requestDto.getUsername();
    }catch (IllegalArgumentException e){
      throw new IllegalArgumentException("해당 유저는 이미 존재 합니다");
    }
  }

  public Long login(LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
    String username = requestDto.getUsername();
    UserNamePassword user;
    String token;
    try {
       user = userRepository.findByUsername(username);

       if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
         throw new IllegalArgumentException("유저의 아이디 혹은 비밀 번호가 틀렸습니다.");
       }
       token = jwtUtil.createToken(user.getId());
    }catch (Exception e){
      throw new NoSuchElementException("유저의 아이디 혹은 비밀 번호가 틀렸습니다.");
    }
    jwtUtil.addJwtToHeader(token,httpServletResponse);
    return jwtUtil.giveUserId(token);
  }
  //
  @Transactional
  public Long update(UpdateRequestDto requestDto, User user) {
    User updateuser=userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);
    updateuser.update(requestDto);
    return updateuser.getId();
  }
  @Transactional
  public Long delete(User user) throws NoSuchElementException {
    try {
      userRepository.delete(user);
    }catch (Exception e){
      throw new NoSuchElementException("해당 유저를 지우는데 실패");
    }
    return user.getId();
  }
  public Long userLogout(HttpServletResponse servletResponse) {
    servletResponse.setHeader(Auth,null);
    return 200L;
  }
}
