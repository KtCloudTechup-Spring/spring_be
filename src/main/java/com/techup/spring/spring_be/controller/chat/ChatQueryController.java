package com.techup.spring.spring_be.controller.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.service.chat.ChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatQueryController {

    private final ChatQueryService chatQueryService;

    @GetMapping("/{communityId}")
    public List<ChatMessageResponse> getMessages(
            @PathVariable Long communityId
    ) {
        return chatQueryService.getMessages(communityId);
    }
}

