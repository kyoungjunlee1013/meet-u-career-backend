package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.entity.QCommunityPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QueryDSL 커뮤니티 게시글 인기순 조회 구현
 */
@Repository
@RequiredArgsConstructor
public class CommunityPostQueryRepositoryImpl implements CommunityPostQueryRepository {

  private final JPAQueryFactory queryFactory;

  /**
   * 좋아요 순 인기 게시글 조회 (전체)
   */
  @Override
  public List<CommunityPost> findPopularPosts(int limit) {
    QCommunityPost post = QCommunityPost.communityPost;

    return queryFactory.selectFrom(post)
        .where(post.status.eq(CommunityPost.Status.ACTIVE))
        .orderBy(post.likeCount.desc(), post.createdAt.desc())
        .limit(limit)
        .fetch();
  }

  /**
   * 해시태그 기준 인기글 조회
   */
  @Override
  public List<CommunityPost> findPopularPostsByTag(Long tagId, int limit) {
    QCommunityPost post = QCommunityPost.communityPost;

    return queryFactory.selectFrom(post)
        .where(
            post.status.eq(CommunityPost.Status.ACTIVE),
            post.tag.id.eq(tagId)
        )
        .orderBy(post.likeCount.desc(), post.createdAt.desc())
        .limit(limit)
        .fetch();
  }

  /**
   * 특정 해시태그 기준으로 게시글을 최신순으로 조회
   */
  @Override
  public List<CommunityPost> findPostsByTagId(Long tagId, int limit) {
    QCommunityPost post = QCommunityPost.communityPost;

    return queryFactory.selectFrom(post)
        .where(
            post.status.eq(CommunityPost.Status.ACTIVE),  // 게시글 상태가 ACTIVE인 것만
            post.tag.id.eq(tagId)                         // tagId로 필터링
        )
        .orderBy(post.createdAt.desc())                  // 최신순 정렬
        .limit(limit)                                    // 조회 개수 제한
        .fetch();                                        // 결과 리스트 반환
  }
}
