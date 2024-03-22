package com.sparta.scv.usertest.servicetest;

import com.sparta.scv.global.jwt.JwtUtil;
import com.sparta.scv.user.dto.UpdateRequestDto;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import com.sparta.scv.user.service.UserService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository mockRepo;
    @Mock
    JwtUtil jwtUtil;

    RedissonClient redissonClient;

    StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("유저 정보 변경")
    void update() throws InterruptedException {
        //given
        User users = new User(1L);

        UpdateRequestDto requestDto = new UpdateRequestDto();
        AtomicInteger l = new AtomicInteger();
        requestDto.setNickname("테스트용닉 : " + l);

        UserService userService = new UserService(mockRepo, passwordEncoder, jwtUtil,
            redissonClient);

        int threads = 1000;
        // 쓰레드 생성
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        // 주어진 수 만큼 이벤트를 기다림
        CountDownLatch latch = new CountDownLatch(threads);
        AtomicInteger fail = new AtomicInteger();
        //when
        for (int i = 1; i <= threads; i++) {
            // 각 쓰레드에서 사용할 요청 생성

            int finalI = i;

            executorService.submit(() -> {
                try {
                    System.out.println(finalI + "번째 쓰레드 접근 시작");
                    requestDto.setNickname("테스트용닉 : " + l);
                    l.getAndIncrement();
                    try {
                        userService.update(requestDto, users);
                    } catch (Exception e) {
                        System.out.println("다른 누군가가 정보를 수정중입니다!");
                        fail.getAndIncrement();
                    }
                } finally {
                    latch.countDown();
                    System.out.println(finalI + "번째 쓰레드 접근 종료");
                }
            });
        }
        latch.await(); // 모든 쓰레드의 작업이 완료될 때까지 대기
        executorService.shutdown();

        //then
        System.out.println(fail + " : 만큼 정지");
    }
}
