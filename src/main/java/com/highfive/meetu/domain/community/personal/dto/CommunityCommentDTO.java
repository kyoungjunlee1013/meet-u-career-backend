package com.highfive.meetu.domain.community.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCommentDTO {

  private Long id; // 댓글 ID

  private Long postId; // 댓글이 속한 게시글 ID

  private Long accountId; // 댓글 작성자 계정 ID

  private String content; // 댓글 내용

  private Integer status; // 댓글 상태 (0: 정상, 1: 삭제됨)

  private LocalDateTime createdAt; // 댓글 생성일

  private LocalDateTime updatedAt; // 댓글 수정일
}



