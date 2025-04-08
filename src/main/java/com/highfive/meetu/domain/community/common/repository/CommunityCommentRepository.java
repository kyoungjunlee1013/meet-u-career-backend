package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

  // 특정 게시글의 댓글 리스트 (정상 상태만)
  List<CommunityComment> findAllByPostIdAndStatusOrderByCreatedAtAsc(Long postId, Integer status);

}
