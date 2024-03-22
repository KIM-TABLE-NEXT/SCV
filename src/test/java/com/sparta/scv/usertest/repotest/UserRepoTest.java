package com.sparta.scv.usertest.repotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.scv.user.dto.SignupDto;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserNamePassword;
import com.sparta.scv.user.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class UserRepoTest {
  @Autowired
  UserRepository userRepository;
  @Autowired
  RedissonClient redissonClient;
  @Autowired
  StringRedisTemplate stringRedisTemplate;
  String LOCK_KEY = "Lock";

  @Test
  @DisplayName("유저 생성")
  void createUser(){
    SignupDto userdto = new SignupDto();
    userdto.setUsername("testuser");
    userdto.setPassword("testuser2");
    userdto.setCompany("testuser2");
    userdto.setDepartment("testu2ser");
    userdto.setNickname("testuser2");
    User user = new User(userdto);
    userRepository.save(user);

    UserNamePassword ans = userRepository.findByUsername("testuser");
    User ansuser = userRepository.findByid(ans.getId()).orElseThrow(NoSuchElementException::new);
    assertEquals(ansuser.getUsername(),"testuser");
    //userRepository.delete(ansuser);
  }
  @Test
  @DisplayName("유저 정보 변경")
  @Transactional
  void updateUser(){

    SignupDto userdtod = new SignupDto();
    userdtod.setUsername("testuser22");
    userdtod.setPassword("testusers2");
    userdtod.setCompany("testuser2s");
    userdtod.setDepartment("testusers");
    userdtod.setNickname("testuser22");
    User saveuser = new User(userdtod);
    userRepository.save(saveuser);

    ///
    UpdateRequestDto userdtos = new UpdateRequestDto();
    userdtos.setPassword("testuser_update");
    userdtos.setCompany("testuser_update");
    userdtos.setDepartment("testuser_update");
    userdtos.setNickname("testuser_update");
    UserNamePassword ans = userRepository.findByUsername("testuser22");

    User user = userRepository.findByid(ans.getId()).orElseThrow(NoSuchElementException::new);
    RLock lock = redissonClient.getFairLock(LOCK_KEY);
    try {
      boolean isLocked = lock.tryLock(10,60, TimeUnit.SECONDS);
      if(isLocked){
        try {
          User updateuser=userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);
          updateuser.update(userdtos);
        }
        finally {
          lock.unlock();
        }
      }
    } catch (InterruptedException e){
      Thread.currentThread().interrupt();
    }
    User ansuser = userRepository.findByid(ans.getId()).orElseThrow(NoSuchElementException::new);
    assertEquals("testuser_update",ansuser.getNickname());
    userRepository.delete(ansuser);
  }
}
