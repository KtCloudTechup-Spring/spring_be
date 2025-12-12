package com.techup.spring.spring_be.dto.comment;

import com.techup.spring.spring_be.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long id;
    private final Long postId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
