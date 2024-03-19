package com.sparta.scv.board;

import com.sparta.scv.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Long> createBoard(
        @RequestBody BoardRequest boardRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardService.createBoard(userDetails.getUser(), boardRequest));
    }

    @GetMapping
    public List<BoardDto> getBoards(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return boardService.getBoards(userDetails.getUser());
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Long> updateBoard(
        @RequestBody BoardRequest boardRequest,
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardService.updateBoard(boardId, boardRequest, userDetails.getUser()));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Long> deleteBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardService.deleteBoard(boardId, userDetails.getUser()));
    }


}
