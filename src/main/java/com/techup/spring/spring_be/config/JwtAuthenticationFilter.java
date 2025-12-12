package com.techup.spring.spring_be.config;

import com.techup.spring.spring_be.service.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// 요청에 대해 Authorization 헤더에서 JWT 토큰을 꺼내고 → 유효하면 → 로그인된 사용자처럼 SecurityContext에 인증 정보를 넣어주는 필터

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);
        try {

            // 2. 토큰이 있고 아직 인증되지 않은 요청에 대해 처리
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 3. 토큰 검증
                if(jwtTokenProvider.validateToken(token)){
                    // 4. 토큰에서 email 추출 (JWT subject)
                    String email = jwtTokenProvider.getEmail(token);

                    // 5. email로 DB에서 UserDetails(사용자 정보) 조회
                    var userDetails = customUserDetailsService.loadUserByUsername(email);

                    // 6. 인증 객체 생성 (Spring Security가 이해할 수 있는 방식)
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // 7. 인증 객체에 요청 정보를 추가
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. SecurityContext에 인증 정보 저장 → 이 순간 “로그인 된 상태”가 됨
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // 토근 오류 로깅
            System.out.println("JWT 인증 실패 : " + e.getMessage());
        }

        // 9. 다음 필터로 요청 넘김
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 Bearer 토큰만 추출
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
