package com.techup.spring.spring_be.dto.chat;

import com.techup.spring.spring_be.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageResponse {
    private Long chattingRoomId;
    private Long senderId;
    private String senderEmail;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;
    private String userProfileImg;

    @Builder
    public ChatMessageResponse(Long chattingRoomId, Long senderId, String senderEmail,String senderName, String content, LocalDateTime createdAt, String userProfileImg) {
        this.chattingRoomId = chattingRoomId;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.content = content;
        this.createdAt = createdAt;
        this.userProfileImg = userProfileImg;
    }

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getChattingRoom().getId(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getEmail(),
                chatMessage.getSender().getName(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                chatMessage.getSender().getProfileImage()

        );
    }
}
