package com.techup.spring.spring_be.controller.chat;

import com.techup.spring.spring_be.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/{roomId}/join")
    public void join(@PathVariable Long roomId, Principal principal) {
        chatRoomService.joinChatRoom(roomId, principal.getName());
    }

    @PostMapping("/{roomId}/leave")
    public void leave(@PathVariable Long roomId, Principal principal) {
        chatRoomService.leaveChatRoom(roomId, principal.getName());
    }
}
