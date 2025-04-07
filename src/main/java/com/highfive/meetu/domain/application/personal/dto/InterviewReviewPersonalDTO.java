package com.highfive.meetu.domain.application.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 면접 후기 조회/상세용 DTO
 * - 후기 목록 조회, 상세 페이지, 관리자 검토 등 응답에서 사용됨
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReviewPersonalDTO {

  // 후기 기본 정보
  private Long id;                     // 후기 ID
  private String companyName;         // 회사 이름 (Join 필요)
  private String jobCategoryName;     // 직무 이름 (Join 필요)
  private String jobCode;             // 상세 직무 코드
  private String jobName;             // 상세 직무 이름
  private String parentJobCode;       // 상위 직무 코드
  private String parentJobName;       // 상위 직무 이름
  private String interviewYearMonth;  // 면접 연월 (예: 2024-12)
  private Integer rating;             // 전반적 평가 (0: 부정적, 1: 보통, 2: 긍정적)
  private LocalDateTime createdAt;    // 작성일시

  // 상세 정보
  private Integer careerLevel;         // 신입/경력 여부 (0: 신입, 1: 경력)
  private Integer difficulty;          // 면접 난이도 (1~5점)
  private Integer interviewType;       // 면접 유형 (비트마스크)
  private Integer interviewParticipants; // 면접 인원 유형
  private Boolean hasFrequentQuestions; // 자주 묻는 질문 포함 여부
  private String questionsAsked;       // 실제 질문 내용
  private String interviewTip;         // 면접 팁/노하우
  private Integer result;              // 결과 (0: 불합격, 1: 합격, 2: 대기중)

  // 연관 엔티티 ID
  private Long applicationId;          // 연결된 지원서 ID
  private Long companyId;              // 회사 ID
  private Long jobCategoryId;          // 직무 카테고리 ID

  // 상태 정보
  private Integer status;              // 상태값 (0: 활성, 1: 삭제 요청 등)

}
