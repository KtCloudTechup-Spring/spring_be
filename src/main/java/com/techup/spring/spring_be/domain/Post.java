package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 커뮤니티(과정)의 글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    protected Post() {
    }

    public Post(User user, Community community, String title, String content) {
        this.user = user;
        this.community = community;
        this.title = title;
        this.content = content;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Community getCommunity() {
        return community;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // 변경 메소드
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
