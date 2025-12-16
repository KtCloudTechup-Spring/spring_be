package com.techup.spring.spring_be.service;

import com.techup.spring.spring_be.domain.Community;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.CommentRepository;
import com.techup.spring.spring_be.repository.CommunityRepository;
import com.techup.spring.spring_be.repository.FavoriteRepository;
import com.techup.spring.spring_be.repository.PostRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;

    /** 게시글 생성 */
    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        Post post = new Post(user, community, request.getTitle(), request.getContent());
        Post saved = postRepository.save(post);

        long commentCount = 0;
        long favoriteCount = 0;
        boolean favorited = false;

        return new PostResponse(saved, commentCount, favoriteCount, favorited);
    }

    /** 단건 조회 (로그인 유저 기준 favorited 포함하려면 userId를 받을 수도 있음) */
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        long commentCount = commentRepository.countByPost(post);
        long favoriteCount = favoriteRepository.countByPost(post);

        // 비로그인 조회도 가능하게 해둔 상태라면 favorited는 기본 false
        boolean favorited = false;

        return new PostResponse(post, commentCount, favoriteCount, favorited);
    }

    /** 로그인 유저까지 받아서 favorited 포함한 단건 조회가 필요하면 이걸 사용 */
    public PostResponse getPost(Long postId, Long userIdOrNull) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        long commentCount = commentRepository.countByPost(post);
        long favoriteCount = favoriteRepository.countByPost(post);

        boolean favorited = false;
        if (userIdOrNull != null) {
            User user = userRepository.findById(userIdOrNull)
                    .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
            favorited = favoriteRepository.existsByUserAndPost(user, post);
        }

        return new PostResponse(post, commentCount, favoriteCount, favorited);
    }

    /** 커뮤니티별 목록 조회 */
    public Page<PostResponse> getPostsByCommunity(Long communityId, int page, int size, Long userIdOrNull) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size);

        User user = null;
        if (userIdOrNull != null) {
            user = userRepository.findById(userIdOrNull)
                    .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        }

        User finalUser = user;
        return postRepository.findByCommunityOrderByCreatedAtDesc(community, pageable)
                .map(post -> {
                    long commentCount = commentRepository.countByPost(post);
                    long favoriteCount = favoriteRepository.countByPost(post);
                    boolean favorited = (finalUser != null) && favoriteRepository.existsByUserAndPost(finalUser, post);
                    return new PostResponse(post, commentCount, favoriteCount, favorited);
                });
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

        long commentCount = commentRepository.countByPost(post);
        long favoriteCount = favoriteRepository.countByPost(post);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        boolean favorited = favoriteRepository.existsByUserAndPost(user, post);

        return new PostResponse(post, commentCount, favoriteCount, favorited);
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

    /** 내가 작성한 글 조회 */
    public Page<PostResponse> getMyPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        User finalUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        return postRepository.findByUserId(userId, pageable).map(
                post -> {
                    long commentCount = commentRepository.countByPost(post);
                    long favoriteCount = favoriteRepository.countByPost(post);
                    boolean favorited = (finalUser != null) && favoriteRepository.existsByUserAndPost(finalUser, post);
                    return new PostResponse(post, commentCount, favoriteCount, favorited);
                }
        );
    }
}