package com.highfive.meetu.domain.application.personal.dto;

import lombok.*;

/**
 * 면접 후기 작성용 DTO
 * - 프론트에서 서버로 후기 등록 시 사용되는 요청 DTO
 * - Entity에 존재하는 컬럼만 포함 (id, createdAt, status 등은 제외)
 * - profileId는 서버에서 JWT 또는 세션으로 처리
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReviewCreateDTO {

  private Long companyId; // 후기 작성 대상 기업 ID
  private Long jobCategoryId; // 후기 작성 대상 직무 카테고리 ID
  private Integer careerLevel; // 면접 당시 신입/경력 여부 (0: 신입, 1: 경력)
  private String interviewYearMonth; // 면접 연도-월 (예: 2024-12)
  private Integer rating; // 면접 전반 평가 (0: 부정적, 1: 보통, 2: 긍정적)
  private Integer difficulty; // 난이도 (1~5점)
  private Integer interviewType; // 면접 유형 (비트마스크로 저장)
  private Integer interviewParticipants; // 면접 인원 유형 (예: 0: 1:1, 1: 다대1)
  private Boolean hasFrequentQuestions; // 자주 나오는 질문 여부 체크
  private String questionsAsked; // 면접 질문 내용 (줄바꿈 포함된 텍스트)
  private String interviewTip; // 면접 팁/노하우
  private Integer result; // 면접 결과 (0: 불합격, 1: 합격, 2: 대기중)
  private Long applicationId; // 연결된 지원서 ID
}
