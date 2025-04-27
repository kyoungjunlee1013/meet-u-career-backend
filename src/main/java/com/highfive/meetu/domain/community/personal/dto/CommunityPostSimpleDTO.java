package com.highfive.meetu.domain.community.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostSimpleDTO {
  private Long id;
  private String title;
  private int likeCount;
  private int commentCount;
  private LocalDateTime createdAt;

  // ✅ 이 안에 static 메서드 넣어야 해
  public static CommunityPostSimpleDTO fromEntity(com.highfive.meetu.domain.community.common.entity.CommunityPost post) {
    return CommunityPostSimpleDTO.builder()
        .id(post.getId())
        .title(post.getTitle())
        .likeCount(post.getLikeCount())
        .commentCount(post.getCommentCount())
        .createdAt(post.getCreatedAt())
        .build();
  }
}
