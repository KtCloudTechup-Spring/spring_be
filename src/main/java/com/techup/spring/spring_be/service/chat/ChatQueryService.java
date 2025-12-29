package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.dto.chat.ChatParticipantResponse;
import com.techup.spring.spring_be.repository.ChatMessageRepository;
import com.techup.spring.spring_be.repository.ChatParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public List<ChatMessageResponse> getMessages(Long communityId) {
        return chatMessageRepository
                .findByChattingRoomIdOrderByCreatedAtAsc(communityId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    public List<ChatParticipantResponse> myChatParticipant(Long userId) {
        List<ChatParticipant> chatParticipant = chatParticipantRepository.findByUserIdAndLeftAtIsNull(userId);

        return chatParticipant.stream()
                .map(cp -> ChatParticipantResponse.from(cp.getChattingRoom()))
                .toList();
    }
}
