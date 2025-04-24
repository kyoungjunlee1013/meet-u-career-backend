package com.highfive.meetu.domain.application.personal.dto;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReviewDTO {

  private Long id;
  private String company;
  private String position;
  private String date;
  private String jobCategory;
  private int rating;
  private int difficulty;
  private int interviewType;
  private int interviewParticipants;
  private String questionsAsked;
  private String interviewTip;
  private int result;
  private String createdAt;
  private String updatedAt;
  private String logo;

  public static InterviewReviewDTO fromEntity(InterviewReview review) {
    return InterviewReviewDTO.builder()
        .id(review.getId())
        .company(review.getCompany().getName())
        .position("포지션명")
        .date(review.getCreatedAt().toString()) // ✅ 여기만 수정
        .jobCategory("직무 카테고리")
        .rating(review.getRating())
        .difficulty(review.getDifficulty())
        .interviewType(review.getInterviewType())
        .interviewParticipants(review.getInterviewParticipants())
        .questionsAsked(review.getQuestionsAsked())
        .interviewTip(review.getInterviewTip())
        .result(review.getResult())
        .createdAt(review.getCreatedAt().toString())
        .updatedAt(review.getUpdatedAt().toString())
        // .logo(review.getCompany().getLogoUrl())
        .build();
  }

}
