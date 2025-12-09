package com.techup.spring.spring_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 일단 개발 단계에서는 CSRF 끄기
                .csrf(csrf -> csrf.disable())

                // 어떤 요청을 허용할지 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll() // 헬스 체크는 누구나
                        .anyRequest().permitAll()                  // 나머지도 일단 전부 허용
                )

                // 기본 로그인 폼 사용 안 함
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}