package com.techup.spring.spring_be.dto.chat;

import com.techup.spring.spring_be.domain.ChatParticipant;
import lombok.Getter;

@Getter
public class ChatUserParticipantResponse {
    private Long chattingRoomId;
    private Long userId;
    private String userName;

    public ChatUserParticipantResponse(Long chattingRoomId, Long userId, String userName) {
        this.chattingRoomId = chattingRoomId;
        this.userId = userId;
        this.userName = userName;
    }

    public static ChatUserParticipantResponse from(ChatParticipant participant) {
        return new ChatUserParticipantResponse(
                participant.getChattingRoom().getId(),
                participant.getUser().getId(),
                participant.getUser().getName()
        );
    }

}



