package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewApplicationDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 면접 후기 관련 개인 사용자 서비스
 */
@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;

  /**
   * 후기 작성 가능 여부를 판단합니다.
   * 조건: 면접 상태가 완료(status=3)이고, 해당 지원서로 작성된 리뷰가 없을 경우
   */
  public boolean canWriteReview(Application application) {
    return application.getStatus() == 3 &&
        !interviewReviewRepository.existsByApplicationId(application.getId());
  }

  /**
   * Application 리스트를 InterviewReviewApplicationDTO로 변환하며,
   * 후기 작성 가능 여부를 함께 반환합니다.
   */
  public List<InterviewReviewApplicationDTO> toDTOList(List<Application> applications) {
    return applications.stream()
        .map(app -> InterviewReviewApplicationDTO.builder()
            .applicationId(app.getId())
            .jobTitle(app.getJobPosting().getTitle())
            .companyName(app.getJobPosting().getCompany().getName())
            .status(app.getStatus())
            .createdAt(app.getCreatedAt())
            .canWriteReview(canWriteReview(app)) // 핵심 조건
            .build()
        ).collect(Collectors.toList());
  }


  public List<InterviewReviewDTO> getReviewsByProfileId(Long profileId) {
    List<InterviewReview> list = interviewReviewRepository.findByProfileId(profileId);
    return list.stream()
        .map(InterviewReviewDTO::fromEntity)
        .collect(Collectors.toList());
  }

}