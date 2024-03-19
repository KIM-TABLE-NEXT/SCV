package com.sparta.scv.global.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scv.global.impl.UserDetailsImpl;
import com.sparta.scv.global.impl.UserDetailsServiceImpl;
import com.sparta.scv.global.jwt.JwtUtil;
import com.sparta.scv.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthorizationFilter extends
    OncePerRequestFilter { // OncePerRequestFilter : 요청 한번마다 필터 돌리기

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    //토큰 받기
    String token = jwtUtil.getTokenFromRequest(httpServletRequest);

    if (Objects.nonNull(token)) {
      // 만약 토큰이 있으면
      if (jwtUtil.validateToken(token)) {
        // 토큰에서 유저정보를 받아와서 info 에 넣음
        String t = token.substring(6);
        Claims info = jwtUtil.getUserInfoFromToken(t);

        // info 에서 아이디 을 뽑아와서 저장
        long id = (long)(int) info.get("userId");


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // Authentication (인증된 정보) 를 담고 있는 Holder
        // SecurityContextHolder 가 가진 값을 통해 인증이 되었나 확인할 수있음.

        UserDetails userDetails; // = userDetailsServiceImpl.loadUserByUsername(username);
        User user = new User(id);
        userDetails = new UserDetailsImpl(user);
        //userDetails 에 유저의 상세 정보를 넣음
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
            null, userDetails.getAuthorities());
        //인증 확인을 하여 아직 인증이 완료되었다는 정보를 보냄

        context.setAuthentication(authentication);
        // 해당 인증정보를 ContextHolder 에 저장

        SecurityContextHolder.setContext(context);
        // 여기까지 하면 @AuthenticationPrincipal 로 해당 내용들을 조회할 수 있음
      } else {
        // 인증정보가  존재하지 않을때
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.getWriter()
            .write("토큰이 유효하지 않습니다.");
        return;
        // 더 필터를 타지 않고 에러응답을 리턴시킴
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}

