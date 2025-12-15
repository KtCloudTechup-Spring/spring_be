package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.techup.spring.spring_be.dto.common.ErrorResponse;

@io.swagger.v3.oas.annotations.tags.Tag(name = "게시글", description = "게시글과 관련된 모든 API 엔드포인트들을 제공합니다.") // 클래스 레벨
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 게시글 생성 (로그인 필요) */
    @Operation(summary = "새로운 게시글 생성", description = "인증된 사용자가 특정 커뮤니티에 새로운 게시글을 생성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글이 성공적으로 생성되었으며, 생성된 게시글 정보를 반환합니다.",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 본문(RequestBody)의 데이터가 유효하지 않습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. 유효한 JWT 토큰이 필요합니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails, // Swagger UI에서 이 파라미터를 숨김
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        PostResponse res = postService.createPost(userId, request);
        return ApiResponse.ok("게시글 생성 성공", res);
    }

    /** 단건 조회 (비로그인 가능: favorited=false 처리) */
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userIdOrNull = (userDetails == null) ? null : getCurrentUserId(userDetails);
        PostResponse res = postService.getPost(postId, userIdOrNull);
        return ApiResponse.ok("게시글 조회 성공", res);
    }

    /** 커뮤니티별 목록 조회(페이징) (비로그인 가능) */
    @GetMapping("/community/{communityId}")
    public ApiResponse<Page<PostResponse>> getPostsByCommunity(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userIdOrNull = (userDetails == null) ? null : getCurrentUserId(userDetails);

        // ✅ 여기서 4번째 인자 userIdOrNull을 넘겨야 에러가 사라짐
        Page<PostResponse> res = postService.getPostsByCommunity(communityId, page, size, userIdOrNull);
        return ApiResponse.ok("게시글 목록 조회 성공", res);
    }

    /** 수정 (로그인 필요) */
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        PostResponse res = postService.updatePost(postId, userId, request);
        return ApiResponse.ok("게시글 수정 성공", res);
    }

    /** 삭제 (로그인 필요) */
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        postService.deletePost(postId, userId);
        return ApiResponse.ok("게시글 삭제 성공");
    }
}
