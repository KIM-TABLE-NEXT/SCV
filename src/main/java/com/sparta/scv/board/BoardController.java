package com.sparta.scv.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public String addBoard(
        @RequestBody Board board
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return null;
    }

    @GetMapping
    public List<Board> getBoards(
        // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return null;
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
