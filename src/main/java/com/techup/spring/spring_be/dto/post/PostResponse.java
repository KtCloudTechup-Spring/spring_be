package com.techup.spring.spring_be.dto.post;

import com.techup.spring.spring_be.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Long id;
    private final Long communityId;
    private final Long userId;

    private final String authorName;
    private final String authorProfileImage;

    private final String title;
    private final String content;

    private final long commentCount;
    private final long favoriteCount;
    private final boolean favorited;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final String imageUrl;

    public PostResponse(Post post, long commentCount, long favoriteCount, boolean favorited){
        this.id = post.getId();
        this.communityId = post.getCommunity().getId();
        this.userId = post.getUser().getId();

        this.authorName = post.getUser().getName();
        this.authorProfileImage = post.getUser().getProfileImage();

        this.title = post.getTitle();
        this.content = post.getContent();

        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
        this.favorited = favorited;

        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.imageUrl = post.getImageUrl();
    }
}
