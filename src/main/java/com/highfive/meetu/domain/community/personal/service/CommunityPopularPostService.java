package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostSimpleDTO;
import com.highfive.meetu.domain.community.common.repository.CommunityPostQueryRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPopularPostService {

  private final CommunityPostQueryRepository communityPostQueryRepository;

  /**
   * 전체 인기 게시글 조회 (좋아요 수 기준 정렬)
   */
  public List<CommunityPostSimpleDTO> getPopularPosts(int limit) {
    // ❗ findPopularPostOnePerTag를 사용
    List<CommunityPostSimpleDTO> posts = communityPostQueryRepository.findPopularPostOnePerTag(limit)
        .stream()
        .map(CommunityPostSimpleDTO::fromEntity) // 필요하면 변환
        .toList();
    return posts;
  }

  /**
   * 특정 해시태그별 인기 게시글 조회 (좋아요 수 기준 정렬)
   */
  public List<CommunityPostSimpleDTO> getPopularPostsByTag(Long tagId, int limit) {
    List<CommunityPostSimpleDTO> posts = communityPostQueryRepository.findPopularPostsByTag(tagId, limit)
        .stream()
        .map(CommunityPostSimpleDTO::fromEntity) // 필요하면 변환
        .toList();
    return posts;
  }

  public CommunityPostSimpleDTO findPostById(Long postId) {
    CommunityPost post = communityPostQueryRepository.findPostById(postId)
        .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
    return CommunityPostSimpleDTO.fromEntity(post);
  }



}
