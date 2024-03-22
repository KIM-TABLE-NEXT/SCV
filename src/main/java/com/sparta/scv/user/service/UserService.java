package com.sparta.scv.user.service;

import com.esotericsoftware.minlog.Log;
import com.sparta.scv.global.jwt.JwtUtil;
import com.sparta.scv.user.dto.LoginRequestDto;
import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserNamePassword;
import com.sparta.scv.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private static final String Auth = "Authorization";
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  //redis
  private final RedissonClient redissonClient;
  private final StringRedisTemplate stringRedisTemplate;
  private static final String LOCK_KEY = "Lock";

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

  // 사실 회원 정보 수정에 캐싱 이나 동시 접근을 할필요가 없지만 공부 해볼겸 해봤습니다.
  @Transactional
  @CachePut(value = "User", key = "#user.getId", cacheManager = "cacheManager")
  public Long update(UpdateRequestDto requestDto, User user) {
    RLock lock = redissonClient.getFairLock(LOCK_KEY);
    Long returnlong = 404L;
    try {
      boolean isLocked = lock.tryLock(10,60, TimeUnit.SECONDS);
      if(isLocked){
        try {
          User updateuser=userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);
          updateuser.update(requestDto);
          returnlong = updateuser.getId();
        }
        finally {
          lock.unlock();
        }
      }
    } catch (InterruptedException e){
      Thread.currentThread().interrupt();
    }
    return returnlong;
  }

  @Transactional
  @CacheEvict(value = "User", key = "#user.getId", cacheManager = "cacheManager")
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
