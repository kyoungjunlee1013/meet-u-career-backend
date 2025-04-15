package com.highfive.meetu.domain.community.common.repository;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.entity.QCommunityPost;
import com.highfive.meetu.domain.community.common.entity.QCommunityTag;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostListDTO;
import com.highfive.meetu.domain.community.personal.dto.QCommunityPostListDTO;
import com.highfive.meetu.domain.user.common.entity.QAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 QueryDSL 커뮤니티 게시글 복합 조회 쿼리 구현체
 */
@Repository
@RequiredArgsConstructor
public class CommunityPostQueryRepositoryImpl implements CommunityPostQueryRepository {

  private final JPAQueryFactory queryFactory;
  private final QCommunityPost post = QCommunityPost.communityPost;


  // 해시태그별 인기글 조회 (오른쪽 영역)
  @Override
  public List<CommunityPost> findPopularPostsByTag(Long tagId, int limit) {
    return queryFactory.selectFrom(post)
        .where(
            post.status.eq(CommunityPost.Status.ACTIVE),
            post.tag.id.eq(tagId)
        )
        .orderBy(post.likeCount.desc(), post.createdAt.desc())
        .limit(limit)
        .fetch();
  }

  // 해시태그별 게시글 조회 (최신 순 / 중앙 영역)
  @Override
  public List<CommunityPost> findPostsByTagId(Long tagId, int limit) {
    return queryFactory.selectFrom(post)
        .where(
            post.status.eq(CommunityPost.Status.ACTIVE),
            post.tag.id.eq(tagId)
        )
        .orderBy(post.createdAt.desc())
        .limit(limit)
        .fetch();
  }


  // 전체 인기글 조회 (해시태그별로 1개씩 뽑아서 조회 / 오른쪽 영역)
  @Override
  public List<CommunityPost> findPopularPostOnePerTag(int limit) {
    List<CommunityPost> topLiked = queryFactory.selectFrom(post)
        .where(post.status.eq(CommunityPost.Status.ACTIVE))
        .orderBy(post.likeCount.desc(), post.createdAt.desc())
        .limit(100) // 후보군 충분히 확보
        .fetch();

    Map<Long, CommunityPost> bestByTag = new LinkedHashMap<>();

    for (CommunityPost p : topLiked) {
      Long tagId = p.getTag().getId();
      if (!bestByTag.containsKey(tagId)) {
        bestByTag.put(tagId, p);
      }
      if (bestByTag.size() >= limit) break;
    }

    return new ArrayList<>(bestByTag.values());
  }

  @Override
  public List<CommunityPostListDTO> findPostListWithWriterAndTag(int limit) {
    // QueryDSL Q타입 선언
    QCommunityPost post = QCommunityPost.communityPost;
    QCommunityTag tag = QCommunityTag.communityTag;
    QAccount account = QAccount.account;

    return queryFactory
        .select(new QCommunityPostListDTO(
            post.id,               // 게시글 ID
            post.title,            // 제목
            post.content,          // 내용
            tag.id,                // 해시태그 ID
            tag.name,              // 해시태그 이름
            account.id,            // 작성자 ID
            account.name,      // 작성자 이름
            post.postImageKey,     // S3 Key → 이후 URL로 변환
            post.likeCount,        // 좋아요 수
            post.commentCount,     // 댓글 수
            post.createdAt         // 작성일
        ))
        .from(post)
        .join(post.tag, tag)          // 게시글 → 해시태그 조인
        .join(post.account, account) // 게시글 → 작성자 조인
        .where(post.status.eq(CommunityPost.Status.ACTIVE)) // 활성 게시글만
        .orderBy(post.createdAt.desc())                     // 최신순 정렬
        .limit(limit)                                       // 조회 개수 제한
        .fetch();                                           // 결과 리스트 반환
  }

}