package com.techup.spring.spring_be.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chat")
public class Chat extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 방에서 보낸 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChattingRoom room;

    // 누가 보냈는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    protected Chat() {
    }

    public Chat(ChattingRoom room, User user, String message) {
        this.room = room;
        this.user = user;
        this.message = message;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public ChattingRoom getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
