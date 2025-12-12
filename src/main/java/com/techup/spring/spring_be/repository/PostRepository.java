package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Community;
import com.techup.spring.spring_be.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCommunityOrderByCreatedAtDesc(Community community, Pageable pageable);
}