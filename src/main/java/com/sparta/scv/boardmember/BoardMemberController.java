package com.sparta.scv.boardmember;

import com.sparta.scv.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/boardmembers")
@RequiredArgsConstructor
public class BoardMemberController {

    private final BoardMemberService boardMemberService;

    @PostMapping
    public ResponseEntity<Long> addBoardMember(
        @RequestBody BoardMemberRequest boardMemberRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardMemberService.addBoardMember(boardMemberRequest, userDetails.getUser()));
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteBoardMember(
        @RequestBody BoardMemberRequest boardMemberRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity
            .status(200)
            .body(boardMemberService.deleteBoardMember(boardMemberRequest, userDetails.getUser()));
    }
}
