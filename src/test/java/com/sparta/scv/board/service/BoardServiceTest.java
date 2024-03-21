package com.sparta.scv.board.service;

import com.sparta.scv.board.dto.BoardRequest;
import com.sparta.scv.user.entity.User;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @Test
    public void updateBoardConcurrencyTest() throws InterruptedException {
        User user = new User(1L); // 사용자 생성 로직 가정
        BoardRequest initialRequest = new BoardRequest("Initial Name", "Initial Description", "Initial Color");
        Long boardId = boardService.createBoard(user, initialRequest);

        int numberOfThreads = 10;
        // 쓰레드 생성
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        // 주어진 수 만큼 이벤트를 기다림
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 1; i <= numberOfThreads; i++) {
            // 각 쓰레드에서 사용할 요청 생성
            BoardRequest updateRequest = new BoardRequest("Update Name " + i, "Update Description " + i, "Update Color " + i);

            int finalI = i;
            executorService.submit(() -> {
                try {
                    System.out.println(finalI + "번째 쓰레드 접근 시작");
                    boardService.updateRockBoardTest(boardId, updateRequest, user, finalI);
                } finally {
                    latch.countDown();
                    System.out.println(finalI + "번째 쓰레드 접근 종료");
                }
            });
        }

        latch.await(); // 모든 쓰레드의 작업이 완료될 때까지 대기
        executorService.shutdown();
    }


}