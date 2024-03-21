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
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE,
    connection = EmbeddedDatabaseConnection.H2)
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
    UpdateRequestDto userdto = new UpdateRequestDto();
    userdto.setPassword("testuser_update");
    userdto.setCompany("testuser_update");
    userdto.setDepartment("testuser_update");
    userdto.setNickname("testuser_update");
    UserNamePassword ans = userRepository.findByUsername("testuser");

    User user = userRepository.findByid(ans.getId()).orElseThrow(NoSuchElementException::new);
    RLock lock = redissonClient.getFairLock(LOCK_KEY);
    try {
      boolean isLocked = lock.tryLock(10,60, TimeUnit.SECONDS);
      if(isLocked){
        try {
          User updateuser=userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);
          updateuser.update(userdto);
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
