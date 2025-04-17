package com.highfive.meetu.domain.community.personal.dto;

import com.highfive.meetu.domain.community.common.entity.CommunityComment;
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


  public static CommunityCommentDTO fromEntity(CommunityComment entity) {
    return CommunityCommentDTO.builder()
        .id(entity.getId())                         // 댓글 ID
        .postId(entity.getPost().getId())           // 게시글 ID
        .accountId(entity.getAccount().getId())     // 작성자 ID
        .content(entity.getContent())               // 댓글 내용
        .status(entity.getStatus())                 // 상태값 (0:정상, 1:삭제됨)
        .createdAt(entity.getCreatedAt())           // 생성일
        .updatedAt(entity.getUpdatedAt())           // 수정일
        .build();
  }
}



