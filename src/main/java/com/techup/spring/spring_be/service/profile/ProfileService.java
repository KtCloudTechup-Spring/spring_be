package com.techup.spring.spring_be.service.profile;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.user.ChangeProfileRequest;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse changeProfile(String email, ChangeProfileRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 값 변경
        user.setName(request.getName());

        return new UserResponse(
                user.getEmail(),        // 기존 값
                user.getName(),         // 변경된 값
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );

    }
}
