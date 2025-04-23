package com.highfive.meetu.domain.application.personal.dto;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReviewPersonalDTO {

  private Long id;
  private String companyName;
  private String jobCategoryName;
  private String interviewYearMonth;
  private Integer rating;
  private LocalDateTime createdAt;

  private Integer careerLevel;
  private Integer difficulty;
  private Integer interviewType;
  private Integer interviewParticipants;
  private Boolean hasFrequentQuestions;
  private String questionsAsked;
  private String interviewTip;
  private Integer result;

  private Long applicationId;
  private Long companyId;
  private Long jobCategoryId;
  private Integer status;

  // DTO 변환용 메서드
  public static InterviewReviewPersonalDTO from(InterviewReview review) {
    return InterviewReviewPersonalDTO.builder()
        .id(review.getId())
        .companyName(review.getCompany().getName())
        .jobCategoryName(review.getJobCategory().getJobName())
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
        .jobCategoryId(review.getJobCategory().getId())
        .status(review.getStatus())
        .build();
  }
}
