package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewReviewService {
  private final InterviewReviewRepository interviewReviewRepository;

  public List<InterviewCompanySummaryDTO> searchCompaniesWithReview(String keyword) {
    return interviewReviewRepository.searchCompaniesWithReview(keyword);
  }
  /**
   * 면접 후기 작성된 기업 리스트 조회
   */
  public List<InterviewCompanySummaryDTO> getCompaniesWithReview() {
    return interviewReviewRepository.findCompaniesWithReviews();
  }
}
