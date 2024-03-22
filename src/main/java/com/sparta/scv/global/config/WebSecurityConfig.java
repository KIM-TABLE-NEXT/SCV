package com.sparta.scv.global.config;


import com.sparta.scv.global.filter.AuthorizationFilter;
import com.sparta.scv.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;


    //  Filter 한바퀴 돌림
    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(jwtUtil);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // PasswordEncoder 중에 BCryptPasswordEncoder 사용


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception {

        //CSRF 설정하기 (사이트 간의 요청 위조)
        //CSRF 기능 사용 안할것이니 disable
        httpSecurity.csrf((csrf) -> csrf.disable());

        //JWT 방식을 사용하기 위한 설정
        // STATELESS : 스프링 시큐리티가 생성하지 않고 존재해도 사용안함.
        // Never : 생성하지 않지만 이미 존재하면 사용
        // If_Required : 필요시 생성 ( 기본값 )
        // Always : 항상 세션 생성
        httpSecurity.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                // 접근 허용 설정하기
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/users/login").permitAll() // permitAll() : 접근 허가
                .requestMatchers("/users/signup").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .permitAll()
                //anyRequest() : 위 설정 이외 모두
                .anyRequest().authenticated() // authenticated() : jwt 인증 필요함
        );

        // 필터 관리하기
        // httpSecurity.addFilterBefore(Filter,UsernamePasswordAuthenticationFilter.class)
        // 인증 처리하는 기본필터 UsernamePasswordAuthenticationFilter.class 대신에
        // 직접 만든 authorizationFilter() 필터를 등록하고 사용하겠다.
        httpSecurity.addFilterBefore(authorizationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}