package com.highfive.meetu.domain.community.personal.service;

import com.highfive.meetu.domain.community.common.entity.CommunityComment;
import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.repository.CommunityCommentRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.community.personal.dto.CommunityCommentDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
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

  /**
   * 댓글 등록
   */
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

  /**
   * 게시글 댓글 목록 조회
   */
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
}
