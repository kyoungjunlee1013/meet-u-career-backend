package com.highfive.meetu.domain.application.personal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewListDTO {

  private Long interviewId;       // 인터뷰 ID (면접 ID 또는 applicationId)
  private String companyName;     // 회사 이름
  private String position;        // 지원 직무 (예: 백엔드 개발자)
  private String interviewDate;   // 면접 날짜 (yyyy-MM-dd)
  private boolean hasReview;      // 후기 작성 여부


  private Long companyId;
  private Long jobCategoryId;
  private Long applicationId;
}
