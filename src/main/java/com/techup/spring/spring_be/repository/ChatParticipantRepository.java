package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Optional<ChatParticipant> findByChattingRoomAndUser(ChattingRoom chattingRoom, User user);

    List<ChatParticipant> findByUser(User user);

}
