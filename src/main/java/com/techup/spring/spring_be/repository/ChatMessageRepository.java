package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.ChatMessage;
import com.techup.spring.spring_be.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChattingRoomOrderByCreatedAtAsc(ChattingRoom chattingRoom);

    List<ChatMessage> findByChattingRoomIdOrderByCreatedAtAsc(Long roomId);

}
