package com.techup.spring.spring_be.controller.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageRequest;
import com.techup.spring.spring_be.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/{communityId}")
    public void sendMessage(@DestinationVariable Long communityId,
                            ChatMessageRequest request,
                            Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("Principal is null - WebSocket 인증 실패");
        }

        System.out.println("🔥 메시지 도착: " + request.getContent());

        chatService.sendMessage(
                communityId,
                request,
                principal.getName()
        );
    }

}

//@SendTo 제거
//Redis가 브로드캐스트 담당