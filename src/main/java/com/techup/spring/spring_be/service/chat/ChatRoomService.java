package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.domain.User;

import com.techup.spring.spring_be.repository.ChatParticipantRepository;
import com.techup.spring.spring_be.repository.ChattingRoomRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    public void joinChatRoom(Long roomId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        chatParticipantRepository.findByChattingRoomAndUser(room, user)
                .ifPresentOrElse(
                        participant -> {
                            // 재입장
                            if (participant.getLeftAt() != null) {
                                participant.rejoin(); // leftAt = null
                            }
                        },
                        () -> {
                            // 최초 입장
                            ChatParticipant participant =
                                    new ChatParticipant(room, user);
                            chatParticipantRepository.save(participant);
                        }
                );
    }

    public void leaveChatRoom(Long roomId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        ChatParticipant participant =
                chatParticipantRepository
                        .findByChattingRoomAndUser(room, user)
                        .orElseThrow(() -> new IllegalStateException("참여 기록 없음"));

        if (participant.getLeftAt() == null) {
            participant.leave(); // leftAt = now
        }
    }


}
