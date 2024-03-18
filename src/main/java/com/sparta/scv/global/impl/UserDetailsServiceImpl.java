package com.sparta.scv.global.impl;

import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  // 이메일 값으로 유저 데이터 베이스에서 해당 값을 찾아와서 UserDetails 를 통해 세부 정보를 저장
  public UserDetails getUserDetails(String name) {
    User user = userRepository.findByUsername(name)
        // 만약 username 을 못찾으면 Not Found 로 던지고,
        .orElseThrow(() -> new UsernameNotFoundException("Not Found " + name));
    // username 과 동일한 정보를 찾으면 해당 user 의 각종 값을 담아서 UserDetails 형태로 반환
    return new UserDetailsImpl(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }
}
