package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityTagRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 커뮤니티 게시글 서비스
 */
@Service
@RequiredArgsConstructor
public class CommunityPostService {

  private final CommunityPostRepository communityPostRepository;
  private final CommunityTagRepository communityTagRepository;
  private final AccountRepository accountRepository;

  /**
   * 게시글 작성
   */
  @Transactional
  public CommunityPostDTO createPost(CommunityPostDTO dto) {
    Account account = accountRepository.findById(dto.getAccountId())
        .orElseThrow(() -> new NotFoundException("작성자 정보를 찾을 수 없습니다."));

    CommunityTag tag = communityTagRepository.findById(dto.getTagId())
        .orElseThrow(() -> new NotFoundException("해시태그 정보를 찾을 수 없습니다."));

    CommunityPost post = CommunityPost.builder()
        .account(account)
        .tag(tag)
        .title(dto.getTitle())
        .content(dto.getContent())
        .postImageKey(dto.getPostImageKey())
        .likeCount(0)
        .commentCount(0)
        .status(CommunityPost.Status.ACTIVE)
        .build();

    communityPostRepository.save(post);
    return convertToDTO(post);
  }

  /**
   * 게시글 목록 조회
   */
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getAllActivePosts() {
    List<CommunityPost> posts = communityPostRepository.findAllByStatusOrderByCreatedAtDesc(CommunityPost.Status.ACTIVE);
    return posts.stream().map(this::convertToDTO).toList();
  }

  /**
   * 게시글 상세 조회
   */
  @Transactional(readOnly = true)
  public CommunityPostDTO getPostDetail(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

    return convertToDTO(post);
  }

  /**
   * 게시글 수정
   */
  @Transactional
  public CommunityPostDTO updatePost(CommunityPostDTO dto) {
    CommunityPost post = communityPostRepository.findById(dto.getId())
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("수정할 게시글이 존재하지 않습니다."));

    CommunityTag tag = communityTagRepository.findById(dto.getTagId())
        .orElseThrow(() -> new NotFoundException("해시태그 정보를 찾을 수 없습니다."));

    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setPostImageKey(dto.getPostImageKey());
    post.setTag(tag);

    return convertToDTO(post);
  }

  /**
   * 게시글 삭제 (Soft Delete)
   */
  @Transactional
  public void deletePost(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("삭제할 게시글이 존재하지 않습니다."));

    post.setStatus(CommunityPost.Status.DELETED);
  }

  /**
   * Entity → DTO 변환
   */
  private CommunityPostDTO convertToDTO(CommunityPost post) {
    return CommunityPostDTO.builder()
        .id(post.getId())
        .accountId(post.getAccount().getId())
        .tagId(post.getTag().getId())
        .title(post.getTitle())
        .content(post.getContent())
        .postImageKey(post.getPostImageKey())
        .likeCount(post.getLikeCount())
        .commentCount(post.getCommentCount())
        .status(post.getStatus())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}