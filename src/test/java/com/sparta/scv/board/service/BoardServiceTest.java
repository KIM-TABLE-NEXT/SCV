package com.sparta.scv.board.service;

import com.sparta.scv.board.dto.BoardRequest;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {
    @Autowired
    private BoardService boardService;


    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        user = new User("surname", "surname", "surname", "surname", "surname");
        userRepository.save(user);
    }

    private User user;

    @Test
    public void updateBoardConcurrencyTest() throws InterruptedException {
        BoardRequest initialRequest = new BoardRequest("Initial Name", "Initial Description", "Initial Color");
        Long boardId = boardService.createBoard(user, initialRequest);

        int numberOfThreads = 10;

        // IntStream을 이용하여 병렬 처리
        IntStream.rangeClosed(1, numberOfThreads).parallel().forEach(i -> {
            // 각 쓰레드에서 사용할 요청 생성
            BoardRequest updateRequest = new BoardRequest("Update Name " + i, "Update Description " + i, "Update Color " + i);
            System.out.println(i + "번째 쓰레드 접근 시작");
            boardService.updateRockBoardTest(boardId, updateRequest, user, i);
            System.out.println(i + "번째 쓰레드 접근 종료");
        });

        // 병렬 스트림은 내부적으로 ForkJoinPool을 사용하여 작업을 처리하므로,
        // 모든 작업이 완료될 때까지 기다리는 별도의 명시적 대기 로직이 필요하지 않습니다.
    }


}