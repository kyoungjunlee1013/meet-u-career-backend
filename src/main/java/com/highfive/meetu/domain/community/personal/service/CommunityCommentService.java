package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityComment;
import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.repository.CommunityCommentRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityCommentDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {

  private final CommunityCommentRepository commentRepository;
  private final CommunityPostRepository postRepository;
  private final AccountRepository accountRepository;


  // 댓글 등록
  @Transactional
  public CommunityCommentDTO createComment(CommunityCommentDTO dto) {
    Account account = accountRepository.findById(dto.getAccountId())
        .orElseThrow(() -> new NotFoundException("작성자 정보를 찾을 수 없습니다."));

    CommunityPost post = postRepository.findById(dto.getPostId())
        .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

    CommunityComment comment = CommunityComment.builder()
        .account(account)
        .post(post)
        .content(dto.getContent())
        .status(CommunityComment.Status.ACTIVE)
        .build();

    commentRepository.save(comment);

    // 댓글 수 증가 처리
    post.setCommentCount(post.getCommentCount() + 1);


    return CommunityCommentDTO.builder()
        .id(comment.getId())
        .postId(post.getId())
        .accountId(account.getId())
        .content(comment.getContent())
        .status(comment.getStatus())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }

  // 댓글 목록 조회
  @Transactional(readOnly = true)
  public List<CommunityCommentDTO> getCommentsByPost(Long postId) {
    List<CommunityComment> comments = commentRepository
        .findAllByPostIdAndStatusOrderByCreatedAtAsc(postId, CommunityComment.Status.ACTIVE);

    return comments.stream().map(c -> CommunityCommentDTO.builder()
        .id(c.getId())
        .postId(c.getPost().getId())
        .accountId(c.getAccount().getId())
        .content(c.getContent())
        .status(c.getStatus())
        .createdAt(c.getCreatedAt())
        .updatedAt(c.getUpdatedAt())
        .build()
    ).toList();
  }

  // 내가 쓴 댓글 조회
  @Transactional(readOnly = true)
  public List<CommunityCommentDTO> getMyComments(Long accountId) {
    List<CommunityComment> comments = commentRepository
        .findAllByAccountIdAndStatusOrderByCreatedAtDesc(accountId, CommunityComment.Status.ACTIVE);

    return comments.stream().map(c -> CommunityCommentDTO.builder()
        .id(c.getId())
        .postId(c.getPost().getId())
        .accountId(c.getAccount().getId())
        .content(c.getContent())
        .status(c.getStatus())
        .createdAt(c.getCreatedAt())
        .updatedAt(c.getUpdatedAt())
        .build()
    ).toList();
  }


  // 댓글 수정 (id 체크하여 본인이 등록한 댓글만 수정 가능)
  @Transactional
  public CommunityCommentDTO updateComment(CommunityCommentDTO dto) {
    // 1. 수정 대상 댓글 조회
    CommunityComment comment = commentRepository.findById(dto.getId())
        .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

    // 2. 권한 체크: 요청자와 댓글 작성자 ID가 일치하는지 확인
    if (!comment.getAccount().getId().equals(dto.getAccountId())) {
      throw new BadRequestException("댓글 수정 권한이 없습니다.");
    }

    // 3. 댓글 내용 수정
    comment.setContent(dto.getContent());

    // 4. 결과 DTO로 변환 후 반환
    return CommunityCommentDTO.fromEntity(comment);
  }


  // 댓글 삭제
  @Transactional
  public void deleteComment(Long commentId) {
    CommunityComment comment = commentRepository.findById(commentId)
        .filter(c -> c.getStatus() == CommunityComment.Status.ACTIVE)
        .orElseThrow(() -> new NotFoundException("삭제할 댓글이 존재하지 않습니다."));

    comment.setStatus(CommunityComment.Status.DELETED);

    // 게시글의 댓글 수 감소 처리
    CommunityPost post = comment.getPost();
    post.setCommentCount(Math.max(0, post.getCommentCount() - 1)); // 음수 방지
  }

}
