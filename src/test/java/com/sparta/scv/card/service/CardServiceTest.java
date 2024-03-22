package com.sparta.scv.card.service;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.boardcolumn.entity.BoardColumn;
import com.sparta.scv.boardcolumn.repository.BoardColumnRepository;
import com.sparta.scv.boardmember.entity.BoardMember;
import com.sparta.scv.boardmember.repository.BoardMemberRepository;
import com.sparta.scv.card.dto.request.CardUpdateRequest;
import com.sparta.scv.card.entity.Card;
import com.sparta.scv.card.repository.CardRepository;
import com.sparta.scv.user.entity.User;
import com.sparta.scv.user.repository.UserRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardServiceTest {

    @Autowired
    private CardService cardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardColumnRepository boardColumnRepository;
    @Autowired
    private BoardMemberRepository boardMemberRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void printCard() {
        Card card = cardRepository.findById(1L).orElseThrow();
        System.out.println("제목 : " + card.getTitle());
        System.out.println("내용 : " + card.getDescription());
    }

    @Test
    void setUp() {
        User user = new User(1L, "username", "password", "nickname", "department", "company");
        userRepository.save(user);
        Board board = new Board("Board name", "Board description", "Board color", user);
        boardRepository.save(board);
        BoardMember boardMember = new BoardMember(user, board);
        boardMemberRepository.save(boardMember);
        BoardColumn boardColumn = new BoardColumn("Column name", 1L, board);
        boardColumnRepository.save(boardColumn);
        Card card = new Card(1L, "Card title", "Card description", "Card color", "Start date",
            "End date", boardColumn, user);
        cardRepository.save(card);
    }

    @Test
    public void updateCardConcurrencyTest() throws InterruptedException {
        User user = new User(1L);
        int numberOfThreads = 10;
        // 쓰레드 생성
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        // 주어진 수 만큼 이벤트를 기다림
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 1; i <= numberOfThreads; i++) {
            // 각 쓰레드에서 사용할 요청 생성
            CardUpdateRequest cardUpdateRequest = new CardUpdateRequest(1L, "Update Name " + i,
                "Update Description " + i, "Update Color " + i, "startDate", "endDate");

            int finalI = i;
            executorService.submit(() -> {
                try {
                    System.out.println(finalI + "번째 쓰레드 접근 시작");
                    cardService.updateCard(1L, cardUpdateRequest, user);
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
