package com.techup.spring.spring_be.dto.post;

import com.techup.spring.spring_be.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final Long communityId;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.communityId = post.getCommunity().getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
