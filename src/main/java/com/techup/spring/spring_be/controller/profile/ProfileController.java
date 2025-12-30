package com.techup.spring.spring_be.controller.profile;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.user.ChangeProfileRequest;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.PostService;
import com.techup.spring.spring_be.service.profile.FileStorageService;
import com.techup.spring.spring_be.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final PostService postService;

    @PostMapping("/avatar")
    public ApiResponse<UserResponse> uploadProfileImg(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        String token = authHeader.replace("Bearer", "").trim();
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        // 기본 uploads 폴더에 저장
       // String savedPath = fileStorageService.saveFile(file);

        // aws s3에 이미지 저장
        UserResponse res = profileService.saveAvatar(user, file);

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

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    // 내가 쓴 글 조회
        @GetMapping("/my-posts")
    public ApiResponse<Page<PostResponse>> getMyPosts (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = getCurrentUserId(userDetails);
        Page<PostResponse> res = postService.getMyPosts(userId, page, size);
        return ApiResponse.ok("내 게시글 목록 조회 성공", res);
    }


}
