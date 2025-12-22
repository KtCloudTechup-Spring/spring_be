package com.techup.spring.spring_be.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chattingRoomId;
    // 채팅 메세지 내용
    private String content;

}
