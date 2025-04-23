package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;
  private final CompanyRepository companyRepository;
  public List<InterviewReviewPersonalDTO> findAllByProfileId(Long profileId) {
    return interviewReviewRepository.findAllByProfileIdAndStatus(profileId, 0).stream()
        .map(InterviewReviewPersonalDTO::from)
        .toList();
  }

  public List<InterviewCompanySummaryDTO> getCompaniesWithReview() {
    return interviewReviewRepository.findCompaniesWithReviews();
  }

  public List<InterviewCompanySummaryDTO> searchCompaniesWithReview(String keyword) {
    return interviewReviewRepository.searchCompaniesWithReview(keyword);
  }

  public InterviewCompanySummaryDTO getCompanySummary(Long companyId) {
    Company company = companyRepository.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("해당 기업이 존재하지 않습니다."));

    Long reviewCount = interviewReviewRepository.countByCompanyIdAndStatus(companyId, 0);

    return InterviewCompanySummaryDTO.of(company, reviewCount);
  }


  public List<InterviewReviewPersonalDTO> getTop10RecentReviews() {
    return interviewReviewRepository.findTop10RecentReviews(PageRequest.of(0, 10));
  }

  public List<InterviewReviewPersonalDTO> findByCompanyId(Long companyId) {
    return interviewReviewRepository.findEntitiesByCompanyId(companyId).stream()
        .map(InterviewReviewPersonalDTO::from)
        .toList();
  }
}
