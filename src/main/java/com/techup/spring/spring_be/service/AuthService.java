package com.techup.spring.spring_be.service;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.Community;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.domain.UserRole;
import com.techup.spring.spring_be.dto.auth.AuthResponse;
import com.techup.spring.spring_be.dto.auth.LoginRequest;
import com.techup.spring.spring_be.dto.auth.RegisterRequest;
import com.techup.spring.spring_be.repository.CommunityRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommunityRepository communityRepository;

    // 회원가입
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        // 요구사항대로 고정값 지정
        user.setRole(UserRole.CHALLENGER);

        Long communityID = request.getCommunityId();
        if (communityID == null) {
            throw new IllegalArgumentException("community_id is required.");
        }

        Community community = communityRepository.findById(communityID).orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 없음"));
        user.setCommunity(community);

        user.setProfileImage("default.png");

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );
       //return new AuthResponse("회원가입 완료");
    }

    // 로그인
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(user);
        return new AuthResponse(
                accessToken,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );
    }
}
