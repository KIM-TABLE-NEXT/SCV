package com.sparta.scv.boardcolumn;

import com.sparta.scv.board.entity.Board;
import com.sparta.scv.board.repository.BoardRepository;
import com.sparta.scv.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class BoardColumnServiceTest {

    @Autowired
    private BoardColumnService boardColumnService;

    @Autowired
    private BoardColumnRepository boardColumnRepository;

    @Autowired
    private BoardRepository boardRepository;


    private Board board;
    private BoardColumn boardColumn;

    void createTestBoard() {
        User user = new User(1L);
        this.board = new Board("Test Board", "Test", "Test", user);
        boardRepository.save(board);
    }

    Long createTestColumn() {
        this.boardColumn = new BoardColumn("Check updated : ", 10L, board);
        boardColumnRepository.save(boardColumn);
        return boardColumn.getId();
    }

    @Test
    @DisplayName("redisson 분산락을 적용한 동시성 제어 테스트")
    public void updateColumnNameConcurrencyTest() {
        createTestBoard();
        Long columnId = createTestColumn();
        int numberOfThreads = 10;

        // IntStream을 이용하여 병렬 처리
        IntStream.rangeClosed(1, numberOfThreads).parallel().forEach(i -> {
            String updateName = "update Name" + i;
            System.out.println(i + "번째 쓰레드 접근 시작");
            boardColumnService.updateRockColumnTest(columnId, updateName, i);
            System.out.println(i + "번째 쓰레드 접근 종료");
        });
    }
}