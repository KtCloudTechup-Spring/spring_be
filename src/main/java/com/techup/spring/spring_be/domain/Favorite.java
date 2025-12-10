package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "favorite",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_favorite_user_post",
                        columnNames = {"user_id", "post_id"}
                )
        }
)
public class Favorite extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 좋아요 누른 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 좋아요 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Favorite() {
    }

    public Favorite(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }
}
