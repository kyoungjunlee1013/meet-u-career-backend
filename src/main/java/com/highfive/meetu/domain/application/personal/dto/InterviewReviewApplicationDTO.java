package com.highfive.meetu.domain.application.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 면접 후기 작성을 위한 지원서 요약 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewReviewApplicationDTO {

  private Long applicationId; // 지원서 ID
  private String jobTitle; // 채용 공고 제목
  private String companyName; // 회사 이름
  private Integer status; // 면접 상태 (1: 예정, 2: 취소, 3: 완료)
  private LocalDateTime createdAt; // 지원일
  private Boolean canWriteReview; // 후기 작성 가능 여부
}
