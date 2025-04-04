package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    
}
