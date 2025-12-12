package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;

    // 로그인 회원의 userId 가져오는 헬퍼
    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 게시글 생성 */
    @PostMapping
    public PostResponse createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        return postService.createPost(userId, request);
    }

    /** 단건 조회 */
    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    /** 커뮤니티별 목록 */
    @GetMapping("/community/{communityId}")
    public Page<PostResponse> getPostsByCommunity(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByCommunity(communityId, page, size);
    }

    /** 수정 */
    @PutMapping("/{postId}")
    public PostResponse updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        return postService.updatePost(postId, userId, request);
    }

    /** 삭제 */
    @DeleteMapping("/{postId}")
    public void deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        postService.deletePost(postId, userId);
    }
}
