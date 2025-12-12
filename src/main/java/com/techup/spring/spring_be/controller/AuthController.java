package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.auth.AuthResponse;
import com.techup.spring.spring_be.dto.auth.LoginRequest;
import com.techup.spring.spring_be.dto.auth.RegisterRequest;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse getMyInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer", "");

        // 토큰에서 email 꺼내기
        String email = jwtTokenProvider.getEmail(token);

        // 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );

    }


}