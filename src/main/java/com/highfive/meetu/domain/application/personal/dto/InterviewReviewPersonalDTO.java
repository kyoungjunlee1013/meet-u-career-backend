package com.highfive.meetu.domain.application.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 면접 후기 조회/상세용 DTO
 * - 후기 목록 조회, 상세 페이지, 관리자 검토 등 응답에서 사용
 * - 작성용 DTO보다 더 많은 정보(companyName, createdAt 등)를 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReviewPersonalDTO {

  // 공통 정보 (조회/상세/목록)
  private Long id; // 후기 ID
  private String companyName; // 회사 이름 (조회 시 Join 필요)
  private String jobCategoryName; // 직무 이름 (조회 시 Join 필요)
  private String interviewYearMonth; // 면접 연월 (예: 2024-12)
  private Integer rating; // 전반적 평가
  private LocalDateTime createdAt; // 작성일시

  // 상세/작성용 공통 필드
  private Integer careerLevel; // 신입/경력 여부
  private Integer difficulty; // 면접 난이도
  private Integer interviewType; // 면접 유형 (비트마스크)
  private Integer interviewParticipants; // 면접 인원 유형
  private Boolean hasFrequentQuestions; // 자주 묻는 질문 포함 여부
  private String questionsAsked; // 질문 내용
  private String interviewTip; // 팁/노하우
  private Integer result; // 최종 결과

  // 연관 ID (작성 시 사용될 수 있음)
  private Long applicationId; // 연결된 지원서 ID
  private Long companyId; // 기업 ID
  private Long jobCategoryId; // 직무 카테고리 ID

  // 상태값 (0: 활성, 1: 삭제 요청 등)
  private Integer status;
}

