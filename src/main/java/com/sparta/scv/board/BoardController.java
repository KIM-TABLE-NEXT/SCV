package com.sparta.scv.board;

import com.sparta.scv.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
        @RequestBody BoardRequest boardRequest
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // userDetails에서 가져올 User 임시 생성
        User user = new User();

        Long boardId = boardService.createBoard(user, boardRequest);
        return new ResponseEntity<>(
            new BoardResponse("200", "CREATED", boardId), HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Board> getBoards(
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // userDetails에서 가져올 User 임시 생성
        User user = new User();

        return boardService.getBoards(user);

    }

    @PatchMapping("/{boardId}")
    public String updateBoard(
        @RequestBody Board board,
        @PathVariable int boardId
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return null;
    }

    @DeleteMapping("/{boardId}")
    public String deleteBoard(
        @PathVariable int boardId
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return null;
    }


}
