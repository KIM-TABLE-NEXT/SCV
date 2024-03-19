package com.sparta.scv.aop;

import com.sparta.scv.board.Board;
import com.sparta.scv.boardmember.BoardMemberRepository;
import com.sparta.scv.global.impl.UserDetailsImpl;
import com.sparta.scv.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        Long boardId = extractBoardId(path);

        UserDetailsImpl userDetails =
            (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUser().getId();

        boolean isExistMember = boardMemberRepository.existsByUserIdAndBoardId(userId, boardId);
        if (!isExistMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 보드의 멤버만 접근이 가능합니다");
        }
    }

    private Long extractBoardId(String path) {
        Pattern pattern = Pattern.compile("/boards/(\\d+)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String boardIdStr = matcher.group(1);
            return Long.parseLong(boardIdStr);
        }
        return null;
    }
}
