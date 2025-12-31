package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Profile("!redis")
@Service
@RequiredArgsConstructor
public class SimpleMessagePublisher implements MessagePublisher{
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        System.out.println("🔥 SimpleMessagePublisher 활성화됨");
    }

    @Override
    public void publish(ChatMessageResponse message) {


        System.out.println("🔥 클라이언트 sub");
        System.out.println(message.getChattingRoomId()+ " & " + message.getContent() +" & "+ message.getSenderName());
        messagingTemplate.convertAndSend(
                "/sub/chat/" + message.getChattingRoomId(),
                message
        );
    }
}
