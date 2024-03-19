package com.sparta.scv.board;

import com.sparta.scv.global.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Board 관리", description = "Board 관리에 관련된 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "보드 생성", description = "새로운 보드를 생성합니다.")
    public ResponseEntity<Long> createBoard(
        @RequestBody BoardRequest boardRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardService.createBoard(userDetails.getUser(), boardRequest));
    }

    @GetMapping
    @Operation(summary = "보드 전체 조회", description = "본인이 가입되어있는 모든 보드를 조회합니다.")
    public List<BoardDto> getBoards(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return boardService.getBoards(userDetails.getUser());
    }

    @PatchMapping("/{boardId}")
    @Operation(summary = "보드 수정", description = "보드 이름, 설명, 배경색을 변경합니다.")
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
    @Operation(summary = "보드 삭제", description = "보드를 삭제합니다.")
    public ResponseEntity<Long> deleteBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardService.deleteBoard(boardId, userDetails.getUser()));
    }


}
