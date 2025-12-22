package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageResponse> getMessages(Long communityId) {
        return chatMessageRepository
                .findByChattingRoomIdOrderByCreatedAtAsc(communityId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}
