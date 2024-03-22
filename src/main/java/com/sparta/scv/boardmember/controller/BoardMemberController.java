package com.sparta.scv.boardmember.controller;

import com.sparta.scv.boardmember.dto.BoardMemberRequest;
import com.sparta.scv.boardmember.dto.BoardMembersRequest;
import com.sparta.scv.boardmember.service.BoardMemberService;
import com.sparta.scv.global.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/boardmembers")
@RequiredArgsConstructor
@Tag(name = "보드 회원 관리", description = "보드 회원 관리에 관련된 API")
public class BoardMemberController {

    private final BoardMemberService boardMemberService;

    @PostMapping
    @Operation(summary = "보드 멤버 추가", description = "보드에서 활동할 유저를 추가합니다.")
    public ResponseEntity<Long> addBoardMember(
        @RequestBody BoardMemberRequest boardMemberRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(201)
            .body(boardMemberService.addBoardMember(boardMemberRequest, userDetails.getUser()));
    }

    @PostMapping("/{boardId}")
    @Operation(summary = "보드 멤버 다중 추가", description = "보드에서 활동할 유저를 여러명 추가합니다.")
    public ResponseEntity<String> addBoardMembers(
        @PathVariable Long boardId,
        @RequestBody BoardMembersRequest boardMembersRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(201)
            .body(boardMemberService.addBoardMembers(boardId, userDetails.getUser(),
                boardMembersRequest));
    }


    @DeleteMapping
    @Operation(summary = "보드 멤버 삭제", description = "보드에서 활동 중인 유저를 삭제합니다.")
    public ResponseEntity<Long> deleteBoardMember(
        @RequestBody BoardMemberRequest boardMemberRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardMemberService.deleteBoardMember(boardMemberRequest, userDetails.getUser()));
    }
}
