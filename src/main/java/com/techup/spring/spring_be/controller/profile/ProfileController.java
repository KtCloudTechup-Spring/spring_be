package com.techup.spring.spring_be.controller.profile;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.profile.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileStorageService fileStorageService;

    @PostMapping("/avatar")
    public UserResponse uploadProfileImg(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file")MultipartFile file
            ) throws Exception {

        String token = authHeader.replace("Bearer", "");
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        // 파일 저장
        String savedPath = fileStorageService.saveFile(file);

        // DB에 저장된 경로 업데이트
        user.setProfileImage(savedPath);
        userRepository.save(user);

        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );
    }

}
