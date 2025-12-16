package com.techup.spring.spring_be.controller.profile;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.user.ChangeProfileRequest;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.profile.FileStorageService;
import com.techup.spring.spring_be.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileStorageService fileStorageService;
    private final ProfileService profileService;

    @PostMapping("/avatar")
    public ApiResponse<UserResponse> uploadProfileImg(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String token = authHeader.replace("Bearer", "").trim();
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        String savedPath = fileStorageService.saveFile(file);

        user.setProfileImage(savedPath);
        userRepository.save(user);

        UserResponse res = new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );

        return ApiResponse.ok("프로필 이미지 업로드 성공", res);
    }

    // 프로필 이름 바꾸기
    @PatchMapping("/change-profile")
    public ApiResponse<UserResponse> changeProfileName (
          @AuthenticationPrincipal UserDetails userDetails,
          @Valid @RequestBody ChangeProfileRequest request
    ) {
       UserResponse res = profileService.changeProfile(userDetails.getUsername() ,request);
        return ApiResponse.ok("마이페이지 수정 성공", res);
    }




}
