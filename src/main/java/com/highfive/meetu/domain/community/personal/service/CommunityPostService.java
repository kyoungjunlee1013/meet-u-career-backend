package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import com.highfive.meetu.domain.community.common.repository.CommunityPostQueryRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityTagRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



// 커뮤니티 게시글 서비스
@Service
@RequiredArgsConstructor
public class CommunityPostService {

  private final CommunityPostRepository communityPostRepository;
  private final CommunityTagRepository communityTagRepository;
  private final AccountRepository accountRepository;
  private final CommunityPostQueryRepository communityPostQueryRepository;



  // 게시글 작성
  @Transactional
  public CommunityPostDTO createPost(CommunityPostDTO dto) {

    // 해시태그 필수 선택 검증
    if (dto.getTagId() == null) {
      throw new BadRequestException("해시태그는 반드시 선택해야 합니다.");
    }

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



  // 게시글 목록 조회
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getAllActivePosts() {
    List<CommunityPost> posts = communityPostRepository.findAllByStatusOrderByCreatedAtDesc(CommunityPost.Status.ACTIVE);
    return posts.stream().map(this::convertToDTO).toList();
  }



  // 인기 게시글 조회 (좋아요 순)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPopularPosts(int limit) {
    return communityPostQueryRepository.findPopularPosts(limit).stream()
        .map(this::convertToDTO)
        .toList();
  }



  // 특정 해시태그 기준 인기 게시글 조회
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPopularPostsByTag(Long tagId, int limit) {
    return communityPostQueryRepository.findPopularPostsByTag(tagId, limit).stream()
        .map(this::convertToDTO)
        .toList();
  }



  // 내가 쓴글 조회
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getMyPosts(Long accountId) {
    List<CommunityPost> posts = communityPostRepository
        .findAllByAccountIdAndStatusOrderByCreatedAtDesc(accountId, CommunityPost.Status.ACTIVE);

    return posts.stream().map(this::convertToDTO).toList();
  }



  // 게시글 상세 조회 (게시글을 한번 클릭했을 경우, 댓글과 함께 전체 보이기)
  @Transactional(readOnly = true)
  public CommunityPostDTO getPostDetail(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

    return convertToDTO(post);
  }



  // 해시태그별 게시글 조회
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPostsByTag(Long tagId) {
    List<CommunityPost> posts = communityPostRepository
        .findAllByTagIdAndStatusOrderByCreatedAtDesc(tagId, CommunityPost.Status.ACTIVE);

    return posts.stream().map(this::convertToDTO).toList();
  }



  // 해시태그별 게시글 조회 (QueryDSL 기반)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPostsByTagQueryDSL(Long tagId, int limit) {
    return communityPostQueryRepository.findPostsByTagId(tagId, limit).stream()
        .map(this::convertToDTO)
        .toList();
  }



  // 게시글 수정
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



  // 게시글 삭제
  @Transactional
  public void deletePost(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("삭제할 게시글이 존재하지 않습니다."));

    post.setStatus(CommunityPost.Status.DELETED);
  }



  // Entity → DTO 변환
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