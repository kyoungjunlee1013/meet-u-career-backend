package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    // 상태가 게시 중인 게시글을 최신순으로 조회
    List<CommunityPost> findAllByStatusOrderByCreatedAtDesc(Integer status);

    List<CommunityPost> findAllByAccountIdAndStatusOrderByCreatedAtDesc(Long accountId, Integer status);

    List<CommunityPost> findAllByTagIdAndStatusOrderByCreatedAtDesc(Long tagId, Integer status);



}
