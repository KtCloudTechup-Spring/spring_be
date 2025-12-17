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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public PostResponse createPost(Long userId, PostCreateRequest request, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        // 이미지 업로드는 뒤로(현재는 null)
        Post post = new Post(user, community, request.getTitle(), request.getContent());
        Post saved = postRepository.save(post);

        return new PostResponse(saved, 0, 0, false);
    }

    /** 단건 조회 */
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

    /** 커뮤니티별 목록 조회(검색/정렬) */
    public Page<PostResponse> getPostsByCommunity(
            Long communityId,
            int page,
            int size,
            Long userIdOrNull,
            String q,
            String sort
    ) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size);

        User user = null;
        if (userIdOrNull != null) {
            user = userRepository.findById(userIdOrNull)
                    .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        }
        User finalUser = user;

        Page<Post> posts = "popular".equalsIgnoreCase(sort)
                ? postRepository.searchPopularByCommunity(community, q, pageable)
                : postRepository.searchLatestByCommunity(community, q, pageable);

        return posts.map(p -> {
            long commentCount = commentRepository.countByPost(p);
            long favoriteCount = favoriteRepository.countByPost(p);
            boolean favorited = (finalUser != null) && favoriteRepository.existsByUserAndPost(finalUser, p);
            return new PostResponse(p, commentCount, favoriteCount, favorited);
        });
    }

    /** 수정 */
    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostUpdateRequest request, MultipartFile image) {
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

    /** 삭제 */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    /** ✅ 내가 작성한 글 조회 (ProfileController에서 호출하는 메서드) */
    public Page<PostResponse> getMyPosts(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size);

        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(p -> {
                    long commentCount = commentRepository.countByPost(p);
                    long favoriteCount = favoriteRepository.countByPost(p);
                    boolean favorited = favoriteRepository.existsByUserAndPost(user, p);
                    return new PostResponse(p, commentCount, favoriteCount, favorited);
                });
    }
}
