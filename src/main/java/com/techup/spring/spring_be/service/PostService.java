package com.techup.spring.spring_be.service;

import com.techup.spring.spring_be.domain.Community;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.CommunityRepository;
import com.techup.spring.spring_be.repository.PostRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    /** 게시글 생성 */
    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        Post post = new Post(
                user,
                community,
                request.getTitle(),
                request.getContent()
        );

        Post saved = postRepository.save(post);
        return new PostResponse(saved);
    }

    /** 단건 조회 */
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        return new PostResponse(post);
    }

    /** 커뮤니티별 목록 조회 (페이징) */
    public Page<PostResponse> getPostsByCommunity(Long communityId, int page, int size) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByCommunityOrderByCreatedAtDesc(community, pageable)
                .map(PostResponse::new);
    }

    /** 수정 (작성자만) */
    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        post.update(request.getTitle(), request.getContent());
        return new PostResponse(post);
    }

    /** 삭제 (작성자만) */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }
}