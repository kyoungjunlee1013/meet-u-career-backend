package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
}
