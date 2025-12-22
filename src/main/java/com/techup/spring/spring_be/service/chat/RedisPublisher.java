package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/*메세지 보내는 쪽*/
@Profile("redis")
@Service
@RequiredArgsConstructor
public class RedisPublisher implements MessagePublisher{

    private final RedisTemplate<String, Object> redisTemplate;

    /*public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }*/

    @Override
    public void publish(ChatMessageResponse message) {
        redisTemplate.convertAndSend(
                "chatroom:" + message.getChattingRoomId(),
                message
        );
    }
}
