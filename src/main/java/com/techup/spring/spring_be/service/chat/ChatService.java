package com.techup.spring.spring_be.service.chat;

import com.techup.spring.spring_be.domain.ChatMessage;
import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.chat.ChatMessageRequest;
import com.techup.spring.spring_be.dto.chat.ChatMessageResponse;
import com.techup.spring.spring_be.repository.ChatMessageRepository;
import com.techup.spring.spring_be.repository.ChattingRoomRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    //private final RedisPublisher redisPublisher;
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final MessagePublisher messagePublisher;

    public void sendMessage(Long communityId, ChatMessageRequest request, String email) {
        // 사용자 조회
        User user = userRepository.findByEmail(email).orElseThrow();

        // 채팅방 조회
        ChattingRoom chattingRoom = chattingRoomRepository.findByCommunityId(communityId)
                .orElseThrow(()->new RuntimeException("채팅방을 찾을 수 없습니다."));

        // 메시지 엔티티 생성
        ChatMessage chatMessage = new ChatMessage(
                chattingRoom,
                user,
                request.getContent()
        );

        //  db 저장
        chatMessageRepository.save(chatMessage);


        // response 생성
        ChatMessageResponse response = ChatMessageResponse.builder()
                .chattingRoomId(chattingRoom.getId())
                .senderId(user.getId())
                .senderEmail(user.getEmail())
                .senderName(user.getName())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        System.out.println(request.getContent());

        messagePublisher.publish(response);


        // redis 로 메세지 발행
        /*redisPublisher.publish(
                "chatroom:" + chattingRoom.getId(), response
        );*/

    }
}

//WebSocket → Redis → WebSocket