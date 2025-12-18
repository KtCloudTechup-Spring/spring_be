package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "image_url")
    private String imageUrl;


    //  이미지 없는 생성
    public Post(User user, Community community, String title, String content) {
        this.user = user;
        this.community = community;
        this.title = title;
        this.content = content;
        this.imageUrl = null;
    }

    //  이미지 포함 생성
    public Post(User user, Community community, String title, String content, String imageUrl) {
        this.user = user;
        this.community = community;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    //  수정(이미지 포함)
    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    //  수정(이미지 변경 없을 때)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


    // 변경 메소드
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
