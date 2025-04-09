package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;

import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;

  public List<InterviewReviewPersonalDTO> findAllByProfileId(Long profileId) {
    List<InterviewReview> entities = interviewReviewRepository.findAllByProfileIdAndStatus(profileId, 0);

    return entities.stream()
        .map(review -> InterviewReviewPersonalDTO.builder()
            .id(review.getId())
            .companyName(review.getCompany().getName())
            .jobCategoryName(review.getJobCategory().getJobName())
            .interviewYearMonth(review.getInterviewYearMonth())
            .rating(review.getRating())
            .createdAt(review.getCreatedAt())
            .build())
        .toList();
  }

  public InterviewReviewPersonalDTO findById(Long reviewId) {
    InterviewReview review = interviewReviewRepository.findByIdAndStatus(reviewId, 0)
        .orElseThrow(() -> new NotFoundException("작성된 면접 후기를 찾을 수 없습니다."));

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
        .status(review.getStatus())
        .build();
  }

}
