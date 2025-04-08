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

  public InterviewReview toEntity(Profile profile, Company company, JobCategory jobCategory, Application application) {
    return InterviewReview.builder()
        .profile(profile)
        .company(company)
        .jobCategory(jobCategory)
        .application(application)
        .careerLevel(careerLevel)
        .interviewYearMonth(interviewYearMonth)
        .rating(rating)
        .difficulty(difficulty)
        .interviewType(interviewType)
        .interviewParticipants(interviewParticipants)
        .hasFrequentQuestions(hasFrequentQuestions)
        .questionsAsked(questionsAsked)
        .interviewTip(interviewTip)
        .result(result)
        .status(0)
        .build();
  }
}
