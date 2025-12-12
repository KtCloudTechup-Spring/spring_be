package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Comment;
import com.techup.spring.spring_be.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
}
