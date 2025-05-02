package com.highfive.meetu.domain.application.personal.dto;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.user.common.entity.Profile;
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

    // =====================
    // 후기 기본 정보
    // =====================
    private Long id;                     // 후기 ID
    private String companyName;         // 회사 이름
    private String jobCategoryName;     // 직무 카테고리명 (예: 백엔드 개발)
    private String interviewYearMonth;  // 면접 연월 (예: 2025-03)
    private Integer rating;             // 전반적 평가 (0: 부정, 1: 보통, 2: 긍정)
    private LocalDateTime createdAt;    // 작성일시

    // =====================
    // 직무 상세 정보 (선택)
    // =====================
    private String jobCode;             // 하위 직무 코드
    private String jobName;             // 하위 직무명
    private String parentJobCode;       // 상위 직무 코드
    private String parentJobName;       // 상위 직무명

    // =====================
    // 상세 면접 정보
    // =====================
    private Integer careerLevel;         // 0: 신입, 1: 경력
    private Integer difficulty;          // 면접 난이도 (1~5점)
    private Integer interviewType;       // 면접 유형 (비트마스크)
    private Integer interviewParticipants; // 면접 인원 형태
    private Boolean hasFrequentQuestions; // 자주 묻는 질문 포함 여부
    private String questionsAsked;       // 질문 내용
    private String interviewTip;         // 팁/노하우
    private Integer result;              // 결과 (0: 불합격, 1: 합격, 2: 대기중)

    // =====================
    // 연관 ID
    // =====================
    private Long applicationId;
    private Long companyId;
    private Long jobCategoryId;

    // =====================
    // 기타 상태 정보
    // =====================
    private Integer status;

    // =====================
    // 변환 메서드 (기존 방식 유지)
    // =====================
    public static InterviewReviewPersonalDTO from(InterviewReview review) {
        JobCategory job = review.getJobCategory();

        return InterviewReviewPersonalDTO.builder()
                .id(review.getId())
                .companyName(review.getCompany().getName())
                .jobCategoryName(job.getJobName())
                .jobCode(job.getJobCode())
                .jobName(job.getJobName()) // 하위 직무명 (동일 값이라도 명확히)
                .parentJobCode(job.getParentCategory() != null ? job.getParentCategory().getJobCode() : null)
                .parentJobName(job.getParentCategory() != null ? job.getParentCategory().getJobName() : null)
                .interviewYearMonth(review.getInterviewYearMonth())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .careerLevel(review.getCareerLevel())
                .difficulty(review.getDifficulty())
                .interviewType(review.getInterviewType())
                .interviewParticipants(review.getInterviewParticipants())
                .hasFrequentQuestions(review.getHasFrequentQuestions())
                .questionsAsked(review.getQuestionsAsked())
                .interviewTip(review.getInterviewTip())
                .result(review.getResult())

                .applicationId(review.getApplication().getId())
                .companyId(review.getCompany().getId())
                .jobCategoryId(job.getId())
                .status(review.getStatus())
                .build();
    }
}
