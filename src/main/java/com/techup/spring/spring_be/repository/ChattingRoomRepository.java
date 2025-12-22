package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.ChattingRoom;
import com.techup.spring.spring_be.domain.Community;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    //List<ChattingRoom> findByCommunity(Community community);

    Optional<ChattingRoom> findByCommunity(Community community);

    Optional<ChattingRoom> findByCommunityId(Long communityId);
}
