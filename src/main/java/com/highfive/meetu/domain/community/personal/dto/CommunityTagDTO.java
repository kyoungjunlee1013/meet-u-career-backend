package com.highfive.meetu.domain.community.personal.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTagDTO {

  private Long id; // 태그 ID
  private String name; // 태그 이름
  private Integer status; // 태그 상태 (0: 사용 중, 1: 비활성화)
  private LocalDateTime createdAt; // 태그 생성일
  private LocalDateTime updatedAt; // 태그 수정일
}

