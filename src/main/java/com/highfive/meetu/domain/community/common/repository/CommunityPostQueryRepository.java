package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;

import java.util.List;

/**
 * QueryDSL 기반 커뮤니티 게시글 커스텀 쿼리 리포지토리
 */
public interface CommunityPostQueryRepository {

  /**
   * 좋아요 순 인기 게시글 조회 (전체)
   * @param limit 조회할 게시글 수
   */
  List<CommunityPost> findPopularPosts(int limit);

  /**
   * 특정 해시태그 기준으로 인기 게시글 조회
   * @param tagId 해시태그 ID
   * @param limit 게시글 수
   */
  List<CommunityPost> findPopularPostsByTag(Long tagId, int limit);


  /**
   * 특정 해시태그(tagId)에 해당하는 게시글 목록을 조회 (최신순 정렬)
   * @param tagId 해시태그 ID
   * @param limit 조회 개수 제한
   */
  List<CommunityPost> findPostsByTagId(Long tagId, int limit);
}
