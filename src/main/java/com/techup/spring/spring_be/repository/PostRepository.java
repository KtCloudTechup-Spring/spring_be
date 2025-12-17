package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Community;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ✅ 마이페이지 "내 글" 조회용
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // ✅ 최신순 + 검색(제목/작성자)
    @Query("""
        select p
        from Post p
        join p.user u
        where p.community = :community
          and (:q is null or :q = ''
               or lower(p.title) like lower(concat('%', :q, '%'))
               or lower(u.name) like lower(concat('%', :q, '%')))
        order by p.createdAt desc
    """)
    Page<Post> searchLatestByCommunity(
            @Param("community") Community community,
            @Param("q") String q,
            Pageable pageable
    );

    // ✅ 인기순(좋아요 많은 순) + 검색(제목/작성자)
    @Query(value = """
        select p
        from Post p
        join p.user u
        left join Favorite f on f.post = p
        where p.community = :community
          and (:q is null or :q = ''
               or lower(p.title) like lower(concat('%', :q, '%'))
               or lower(u.name) like lower(concat('%', :q, '%')))
        group by p
        order by count(f) desc, p.createdAt desc
    """,
            countQuery = """
        select count(p)
        from Post p
        join p.user u
        where p.community = :community
          and (:q is null or :q = ''
               or lower(p.title) like lower(concat('%', :q, '%'))
               or lower(u.name) like lower(concat('%', :q, '%')))
    """)
    Page<Post> searchPopularByCommunity(
            @Param("community") Community community,
            @Param("q") String q,
            Pageable pageable
    );
}
