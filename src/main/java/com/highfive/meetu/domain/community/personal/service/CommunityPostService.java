package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import com.highfive.meetu.domain.community.common.repository.CommunityPostQueryRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityTagRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostDTO;
import com.highfive.meetu.domain.community.personal.dto.CommunityPostListDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
  private final CommunityPostQueryRepository communityPostQueryRepository;
  private final S3Service s3Service;

  // 게시글 작성
  @Transactional
  public CommunityPostDTO createPost(CommunityPostDTO dto, MultipartFile image) {
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
        .postImageKey(s3Service.uploadFile(image, "community"))
        .likeCount(0)
        .commentCount(0)
        .status(CommunityPost.Status.ACTIVE)
        .build();

    communityPostRepository.save(post);
    return convertToDTO(post);
  }

  // 전체 게시글 조회 (최신순 / 중앙영역)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getAllActivePosts() {
    return communityPostRepository.findAllByStatusOrderByCreatedAtDesc(CommunityPost.Status.ACTIVE)
        .stream().map(this::convertToDTO).toList();
  }

  // 전체 게시글 조회 (최신순 / 중앙영역 / 단건이 아닌 리스트 조회용)
  @Transactional(readOnly = true)
  public List<CommunityPostListDTO> getPostListWithWriterAndTag(int limit) {
    return communityPostQueryRepository.findPostListWithWriterAndTag(limit).stream()
        .map(dto -> new CommunityPostListDTO(
            dto.getPostId(),
            dto.getTitle(),
            dto.getContent(),
            dto.getTagId(),
            dto.getTagName(),
            dto.getAccountId(),
            dto.getName(), // 작성자 이름 그대로 사용
            s3Service.getImageUrl(dto.getPostImageUrl()), // S3 Presigned URL 변환
            dto.getLikeCount(),
            dto.getCommentCount(),
            dto.getCreatedAt()
        ))
        .toList();
  }


  // 전체 인기 게시글 조회 (해시태그별로 1개씩 뽑아서 조회 / 오른쪽 영역)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPopularPosts(int limit) {
    return communityPostQueryRepository.findPopularPostOnePerTag(limit)
        .stream().map(this::convertToDTO).toList();
  }

  // 해시태그별 인기 게시글 조회 (좋아요 순 / 오른쪽 인기글 영역)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPopularPostsByTag(Long tagId, int limit) {
    return communityPostQueryRepository.findPopularPostsByTag(tagId, limit)
        .stream().map(this::convertToDTO).toList();
  }

  // 내가 쓴 게시글 조회 (최신순)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getMyPosts(Long accountId) {
    return communityPostRepository
        .findAllByAccountIdAndStatusOrderByCreatedAtDesc(accountId, CommunityPost.Status.ACTIVE)
        .stream().map(this::convertToDTO).toList();
  }

  // 게시글 상세 조회 (댓글과 함께 전체 보이기)
  @Transactional(readOnly = true)
  public CommunityPostDTO getPostDetail(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

    return convertToDTO(post);
  }

  // 해시태그별 게시글 조회 (최신순 / 중앙영역)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPostsByTagId(Long tagId) {
    return communityPostRepository
        .findAllByTagIdAndStatusOrderByCreatedAtDesc(tagId, CommunityPost.Status.ACTIVE)
        .stream().map(this::convertToDTO).toList();
  }

  // 해시태그별 게시글 조회 (최신순 / QueryDSL 활용)
  @Transactional(readOnly = true)
  public List<CommunityPostDTO> getPostsByTagQueryDSL(Long tagId, int limit) {
    return communityPostQueryRepository.findPostsByTagId(tagId, limit)
        .stream().map(this::convertToDTO).toList();
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

  // 게시글 삭제 (Soft Delete 방식)
  @Transactional
  public void deletePost(Long postId) {
    CommunityPost post = communityPostRepository.findById(postId)
        .filter(p -> p.getStatus() == CommunityPost.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("삭제할 게시글이 존재하지 않습니다."));

    post.setStatus(CommunityPost.Status.DELETED);
  }

  // Entity → DTO 변환 + 이미지 URL 변환 포함
  private CommunityPostDTO convertToDTO(CommunityPost post) {
    return CommunityPostDTO.builder()
        .id(post.getId())
        .accountId(post.getAccount().getId())
        .tagId(post.getTag().getId())
        .title(post.getTitle())
        .content(post.getContent())
        .postImageKey(post.getPostImageKey())
        .postImageUrl(s3Service.getImageUrl(post.getPostImageKey()))  // presigned URL 변환
        .likeCount(post.getLikeCount())
        .commentCount(post.getCommentCount())
        .status(post.getStatus())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
