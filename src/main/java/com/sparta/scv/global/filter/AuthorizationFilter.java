package com.sparta.scv.global.filter;

import com.sparta.scv.global.impl.UserDetailsImpl;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthorizationFilter extends
    OncePerRequestFilter { 

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        FilterChain filterChain)
        throws ServletException, IOException {

        //토큰 받기
        String token = jwtUtil.getTokenFromRequest(httpServletRequest);

        if (Objects.nonNull(token)) {
            if (jwtUtil.validateToken(token)) {
                String t = token.substring(6);
                Claims info = jwtUtil.getUserInfoFromToken(t);
                long id = (long) (int) info.get("userId");

                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UserDetails userDetails;
                User user = new User(id);
                userDetails = new UserDetailsImpl(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpServletResponse.setContentType("application/json; charset=UTF-8");
                httpServletResponse.getWriter()
                    .write("토큰이 유효하지 않습니다.");
                return;

            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

