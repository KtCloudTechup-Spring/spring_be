package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.ChatParticipant;
import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    // 채팅방 + 유저로 참여 기록 조회
    Optional<ChatParticipant> findByChattingRoomAndUser(ChattingRoom chattingRoom, User user);

    // 채팅방에 현재 참여 중인 유저 목록
    List<ChatParticipant> findByChattingRoomAndLeftAtIsNull(ChattingRoom chattingRoom);

    // 유저가 현재 참여 중인 채팅방들
    List<ChatParticipant> findByUserAndLeftAtIsNull(User user);

    // (선택) 특정 채팅방 참여 여부 체크
    boolean existsByChattingRoomAndUserAndLeftAtIsNull(ChattingRoom chattingRoom, User user);


}
