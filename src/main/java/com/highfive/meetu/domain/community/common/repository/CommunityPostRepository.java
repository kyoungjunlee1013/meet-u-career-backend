package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    // 전체 게시글 조회 (최신순 정렬 / 중앙영역)
    List<CommunityPost> findAllByStatusOrderByCreatedAtDesc(Integer status);

    // 내가 쓴 글 조회 (최신순 정렬)
    List<CommunityPost> findAllByAccountIdAndStatusOrderByCreatedAtDesc(Long accountId, Integer status);

    // 해시태그별 게시글 조회 (최신순 정렬 / 중앙영역)
    List<CommunityPost> findAllByTagIdAndStatusOrderByCreatedAtDesc(Long tagId, Integer status);

    @Query("""
        SELECT COUNT(p.id)
        FROM communityPost p
        WHERE p.createdAt >= :start
    """)
    long countPostsCurrent(@Param("start") LocalDateTime start);

    @Query("""
        SELECT COUNT(p.id)
        FROM communityPost p
        WHERE p.createdAt BETWEEN :start AND :end
    """)
    long countPostsPrevious(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}