package com.techup.spring.spring_be.controller.chat;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.dto.chat.ChatParticipantResponse;
import com.techup.spring.spring_be.dto.chat.ChatUserParticipantResponse;
import com.techup.spring.spring_be.repository.ChatParticipantRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.chat.ChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    @GetMapping("/{communityId}")
    public List<ChatMessageResponse> getMessages(
            @PathVariable Long communityId
    ) {
        return chatQueryService.getMessages(communityId);
    }

    @GetMapping("/me/participant")
    public List<ChatParticipantResponse> myChatParticipant(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        return chatQueryService.myChatParticipant(userId);

    }

    @GetMapping("/{communityId}/participant")
    public List<ChatUserParticipantResponse> userChatParticipant(@PathVariable Long communityId) {
        return chatQueryService.userChatParticipant(communityId);
    }

}

