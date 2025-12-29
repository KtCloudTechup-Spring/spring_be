package com.techup.spring.spring_be.dto.chat;

import com.techup.spring.spring_be.domain.ChattingRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatParticipantResponse {
    private Long chattingRoomId;
    private String chattingRoomName;
    private Long communityId;

    public ChatParticipantResponse(Long chattingRoomId, String chattingRoomName, Long communityId) {
        this.chattingRoomId = chattingRoomId;
        this.chattingRoomName = chattingRoomName;
        this.communityId = communityId;
    }

    public static ChatParticipantResponse from(ChattingRoom room) {
        return new ChatParticipantResponse(
                room.getId(),
                room.getName(),
                room.getCommunity().getId()
        );
    }
}
