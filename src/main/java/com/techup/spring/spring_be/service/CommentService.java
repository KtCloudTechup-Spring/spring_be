package com.techup.spring.spring_be.service;

import com.techup.spring.spring_be.domain.Comment;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.comment.CommentCreateRequest;
import com.techup.spring.spring_be.dto.comment.CommentResponse;
import com.techup.spring.spring_be.dto.comment.CommentUpdateRequest;
import com.techup.spring.spring_be.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse createComment(Long userId, Long postId, CommentCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment(user, post, request.getContent());

        Comment saved = commentRepository.save(comment);
        return new CommentResponse(saved);
    }

    public Page<CommentResponse> getCommentsByPost(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size);

        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable)
                .map(CommentResponse::new);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        comment.update(request.getContent());
        return new CommentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}