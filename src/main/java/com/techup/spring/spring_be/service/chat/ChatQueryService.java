package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.dto.chat.ChatParticipantResponse;
import com.techup.spring.spring_be.dto.chat.ChatUserParticipantResponse;
import com.techup.spring.spring_be.repository.ChatMessageRepository;
import com.techup.spring.spring_be.repository.ChatParticipantRepository;
import com.techup.spring.spring_be.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChattingRoomRepository chattingRoomRepository;

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

    public List<ChatUserParticipantResponse> userChatParticipant(Long communityId) {
        // 체팅방 조회
        ChattingRoom chattingRoom = chattingRoomRepository.findByCommunityId(communityId)
                .orElseThrow(()-> new RuntimeException("채팅방을 찾을 수 없습니다."));

        List<ChatParticipant> userChatParticipant = chatParticipantRepository.findByChattingRoomAndLeftAtIsNull(chattingRoom);

        return userChatParticipant.stream()
                .map(ChatUserParticipantResponse::from)
                .toList();

    }
}
