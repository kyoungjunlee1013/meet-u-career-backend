package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;
  private final CompanyRepository companyRepository;


  /**
   * [마이페이지용] 로그인한 사용자의 후기 전체 조회
   *
   * @param accountId 로그인한 사용자 계정 ID
   * @return 사용자 후기 리스트 (status = 0인 후기만)
   */
  public List<InterviewReviewPersonalDTO> findAllByProfileId(Long accountId) {
    return interviewReviewRepository.findAllByProfileIdAndStatus(accountId, 0).stream()
        .map(InterviewReviewPersonalDTO::from)
        .toList();
  }

  /**
   * [공개용] 면접 후기가 존재하는 기업 목록 반환
   *
   * @return 회사 ID, 이름, 후기 수, 업종 등의 요약 정보 리스트
   */
  public List<InterviewCompanySummaryDTO> getCompaniesWithReview() {
    return interviewReviewRepository.findCompaniesWithReviews();
  }

  /**
   * [검색] 후기 등록 기업 중에서 키워드로 회사명 검색
   *
   * @param keyword 회사명 검색어 (ex. "카카오")
   * @return 검색된 기업 목록
   */
  public List<InterviewCompanySummaryDTO> searchCompaniesWithReview(String keyword) {
    return interviewReviewRepository.searchCompaniesWithReview(keyword);
  }

  /**
   * [기업 상세] 특정 기업의 기본 정보 + 후기 수 요약 반환
   *
   * @param companyId 기업 ID
   * @return 기업 정보 + 등록된 후기 개수
   * @throws NotFoundException 존재하지 않는 기업일 경우
   */
  public InterviewCompanySummaryDTO getCompanySummary(Long companyId) {
    Company company = companyRepository.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("해당 기업이 존재하지 않습니다."));

    Long reviewCount = interviewReviewRepository.countByCompanyIdAndStatus(companyId, 0);

    return InterviewCompanySummaryDTO.of(company, reviewCount);
  }

  /**
   * [최근 인기 후기] 최신순 면접 후기 10건 반환
   *
   * @return 최근 작성된 후기 상위 10건
   */
  public List<InterviewReviewPersonalDTO> getTop10RecentReviews() {
    return interviewReviewRepository.findTop10RecentReviews(PageRequest.of(0, 10));
  }

  /**
   * [기업 상세 후기] 특정 기업에 등록된 전체 면접 후기 반환
   *
   * @param companyId 기업 ID
   * @return 후기 리스트
   */
  public List<InterviewReviewPersonalDTO> findByCompanyId(Long companyId) {
    return interviewReviewRepository.findEntitiesByCompanyId(companyId).stream()
        .map(InterviewReviewPersonalDTO::from)
        .toList();
  }

  /**
   * [후기 단건 조회] 특정 면접 후기 ID로 상세 정보 반환
   *
   * @param reviewId 후기 ID
   * @return 상세 후기 DTO
   * @throws NotFoundException 존재하지 않거나 비활성 상태일 경우
   */
  public InterviewReviewPersonalDTO findById(Long reviewId) {
    InterviewReview review = interviewReviewRepository.findByIdAndStatus(reviewId, 0)
            .orElseThrow(() -> new NotFoundException("면접 후기를 찾을 수 없습니다."));

    return InterviewReviewPersonalDTO.from(review);
  }

}
