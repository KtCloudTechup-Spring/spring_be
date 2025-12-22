package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;

public interface MessagePublisher {
    void publish(ChatMessageResponse message);
}
