package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.comment.CommentCreateRequest;
import com.techup.spring.spring_be.dto.comment.CommentResponse;
import com.techup.spring.spring_be.dto.comment.CommentUpdateRequest;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    // 로그인 회원의 userId 가져오는 헬퍼 (PostController와 동일)
    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 댓글 생성 */
    @PostMapping
    public CommentResponse create(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        return commentService.createComment(userId, postId, request);
    }

    /** 댓글 목록(페이징) */
    @GetMapping
    public Page<CommentResponse> list(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentsByPost(postId, page, size);
    }

    /** 댓글 수정 */
    @PutMapping("/{commentId}")
    public CommentResponse update(
            @PathVariable Long postId, // 경로 통일용(서비스에선 commentId로 검증 가능)
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        return commentService.updateComment(commentId, userId, request);
    }

    /** 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    public void delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        commentService.deleteComment(commentId, userId);
    }
}