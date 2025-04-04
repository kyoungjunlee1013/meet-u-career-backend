package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityTagRepository extends JpaRepository<CommunityTag, Long> {
    
}
