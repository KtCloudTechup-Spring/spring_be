package com.techup.spring.spring_be.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());

        // Redis에서 받은 메시지를 WebSocket 구독자에게 전달
        messagingTemplate.convertAndSend(
                "/sub/chat",
                body
        );
    }

}
