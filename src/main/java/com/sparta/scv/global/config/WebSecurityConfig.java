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

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(jwtUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception {


        httpSecurity.csrf((csrf) -> csrf.disable());


        httpSecurity.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/v1/users/login").permitAll() 
                .requestMatchers("/v1/users/signup").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .permitAll()
                .anyRequest().authenticated() 
        );
      
        httpSecurity.addFilterBefore(authorizationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}