package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityLike;
import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.repository.CommunityLikeRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityLikeDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommunityLikeService {

  private final CommunityLikeRepository communityLikeRepository;
  private final CommunityPostRepository communityPostRepository;
  private final ProfileRepository profileRepository;
  private final EntityManager entityManager;

  // 좋아요 토글 처리, 존재 시 삭제, 없을 시 생성
  @Transactional
  public CommunityLikeDTO toggleLike(Long profileId, Long postId) {

    // 사용자 확인
    // 프로필 확인
    Profile profile = profileRepository.findById(profileId)
        .orElseThrow(() -> new NotFoundException("사용자 프로필 정보를 찾을 수 없습니다."));

    // 게시글 확인
    CommunityPost post = communityPostRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("게시글 정보를 찾을 수 없습니다."));

    // 좋아요 여부 확인
    CommunityLike existingLike = communityLikeRepository.findByProfileIdAndPostId(profileId, postId).orElse(null);

    if (existingLike != null) {
      // 좋아요가 이미 존재하면 → 삭제
      communityLikeRepository.delete(existingLike);
      post.setLikeCount(Math.max(0, post.getLikeCount() - 1));  // 음수 방지
      communityPostRepository.save(post);
      return null;
    }

    // 좋아요가 없으면 → 새로 등록
    CommunityLike like = CommunityLike.builder()
        .profile(profile)
        .post(post)
        .build();
    communityLikeRepository.save(like);
    post.setLikeCount(post.getLikeCount() + 1);

    communityPostRepository.save(post);
    entityManager.flush(); // createdAt 보장

    // DTO 반환
    return CommunityLikeDTO.builder()
        .id(like.getId())
        .profileId(profileId)
        .postId(postId)
        .createdAt(like.getCreatedAt())
        .build();
  }
}