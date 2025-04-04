package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

}
