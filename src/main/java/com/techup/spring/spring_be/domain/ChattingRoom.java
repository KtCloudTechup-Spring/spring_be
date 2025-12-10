package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatting_room")
public class ChattingRoom extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 커뮤니티(과정)의 채팅방인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    // 방 만든 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    protected ChattingRoom() {
    }

    public ChattingRoom(Community community, User createdBy, String name, String description, Integer maxMembers) {
        this.community = community;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.maxMembers = maxMembers;
        this.isActive = true;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Community getCommunity() {
        return community;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    // 상태 변경 메서드
    public void closeRoom() {
        this.isActive = false;
        this.closedAt = LocalDateTime.now();
    }
}
