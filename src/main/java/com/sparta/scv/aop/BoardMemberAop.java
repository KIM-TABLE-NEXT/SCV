package com.sparta.scv.aop;

import com.sparta.scv.boardmember.repository.BoardMemberRepository;
import com.sparta.scv.global.exception.BoardAccessDeniedException;
import com.sparta.scv.global.impl.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "BoardMemberAop")
@Aspect
@Component
public class BoardMemberAop {

    private final BoardMemberRepository boardMemberRepository;


    public BoardMemberAop(BoardMemberRepository boardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
    }

    @Before("@annotation(com.sparta.scv.annotation.BoardMemberOnly)")
    public void checkBoardMember(JoinPoint joinPoint) {
        Long boardId = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BoardIdHolder) {
                boardId = ((BoardIdHolder) arg).getBoardId();
            }
            break;
        }

        UserDetailsImpl userDetails =
            (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUser().getId();

        boolean isExistMember = boardMemberRepository.existsByUserIdAndBoardId(userId, boardId);
        if (!isExistMember) {
            throw new BoardAccessDeniedException("해당 보드의 멤버만 접근이 가능합니다");
        }
    }
}
