package com.techup.spring.spring_be.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "chat_participant",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chat_room_id", "user_id"})
        }
)
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 참여한 채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChattingRoom chattingRoom;

    // 참여한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 참여 시간
    @Column(nullable = false)
    private LocalDateTime joinedAt;

    // 나간 시간 (null이면 참여 중)
    private LocalDateTime leftAt;

    public ChatParticipant(ChattingRoom chattingRoom, User user) {
        this.chattingRoom = chattingRoom;
        this.user = user;
        this.joinedAt = LocalDateTime.now();
    }

    /** 채팅방 나가기 */
    public void leave() {
        this.leftAt = LocalDateTime.now();
    }

    /** 채팅방 재입장 */
    public void rejoin() {
        this.leftAt = null;
        this.joinedAt = LocalDateTime.now();
    }

    /** 현재 참여 중인지 */
    public boolean isActive() {
        return this.leftAt == null;
    }
}

