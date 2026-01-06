package com.techup.spring.spring_be.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Profile("redis")
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            System.out.println("[Redis 수신]");
            System.out.println("현재 서버 포트 => " + System.getProperty("server.port"));
            System.out.println("메시지 = " + body);

            ChatMessageResponse response = objectMapper.readValue(body, ChatMessageResponse.class);

            // 채팅방별 브로드캐스트
            messagingTemplate.convertAndSend(
                    "/sub/chat/" + response.getChattingRoomId(),
                    response
            );
        } catch (Exception e) {
            // 운영에서는 리스너 죽지 않게 throw 금지
            // log.error("RedisSubscriber onMessage error", e);
        }
    }
}

// 메세지 받는 쪽
// Redis → WebSocket 브로드캐스트