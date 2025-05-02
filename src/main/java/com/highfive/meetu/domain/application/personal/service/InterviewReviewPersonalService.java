package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.*;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.job.common.repository.JobCategoryRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;
  private final CompanyRepository companyRepository;
  private final ApplicationRepository applicationRepository;
  private final JobCategoryRepository jobCategoryRepository;
  private final ProfileRepository profileRepository;


    /**
     * [후기 작성 가능 여부 확인]
     * - 조건: 상태(status) = 3 (면접 완료) && 아직 리뷰가 작성되지 않은 경우
     */
    public boolean canWriteReview(Application application) {
        return application.getStatus() == 3 &&
                !interviewReviewRepository.existsByApplicationId(application.getId());
    }

    /**
     * [지원서 리스트 → 후기 작성 가능 여부 DTO 변환]
     * - 프론트에 각 지원서별로 '후기 작성 가능' 표시할 때 사용
     */
    public List<InterviewReviewApplicationDTO> toDTOList(List<Application> applications) {
        return applications.stream()
                .map(app -> InterviewReviewApplicationDTO.builder()
                        .applicationId(app.getId())
                        .jobTitle(app.getJobPosting().getTitle())
                        .companyName(app.getJobPosting().getCompany().getName())
                        .status(app.getStatus())
                        .createdAt(app.getCreatedAt())
                        .canWriteReview(canWriteReview(app))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * [프로필 기준 전체 후기 조회]
     * - 상태 조건 없이 전체 반환 (관리자 검토용 또는 마이페이지용)
     */
    public List<InterviewReviewDTO> getReviewsByProfileId(Long profileId) {
        List<InterviewReview> list = interviewReviewRepository.findByProfileId(profileId);
        return list.stream()
                .map(InterviewReviewDTO::fromEntity)
                .collect(Collectors.toList());
    }

  public Long createReview(InterviewReviewCreateDTO dto, Long profileId) {
    Application application = applicationRepository.findById(dto.getApplicationId())
            .orElseThrow(() -> new NotFoundException("지원 정보를 찾을 수 없습니다."));

    // 이미 해당 applicationId로 작성된 후기가 있는지 확인
    Optional<InterviewReview> existingOpt = interviewReviewRepository
            .findByApplicationId(dto.getApplicationId());


    InterviewReview review;
    if (existingOpt.isPresent()) {
      // 후기 수정
      review = existingOpt.get();
      review.setCompany(companyRepository.getReferenceById(dto.getCompanyId()));
      review.setJobCategory(jobCategoryRepository.getReferenceById(dto.getJobCategoryId()));
      review.setProfile(profileRepository.findByAccountId(profileId)
              .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다.")));
      review.setRating(dto.getRating());
      review.setDifficulty(dto.getDifficulty());
      review.setCareerLevel(dto.getCareerLevel());
      review.setInterviewYearMonth(dto.getInterviewYearMonth());
      review.setInterviewType(dto.getInterviewType());
      review.setInterviewParticipants(dto.getInterviewParticipants());
      review.setHasFrequentQuestions(dto.getHasFrequentQuestions());
      review.setQuestionsAsked(dto.getQuestionsAsked());
      review.setInterviewTip(dto.getInterviewTip());
      review.setResult(dto.getResult());
      // 필요하다면 수정일자 등 추가
    } else {
      // 후기 신규 작성
      review = InterviewReview.builder()
              .application(application)
              .company(companyRepository.getReferenceById(dto.getCompanyId()))
              .jobCategory(jobCategoryRepository.getReferenceById(dto.getJobCategoryId()))
              .profile(profileRepository.findByAccountId(profileId)
                      .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다.")))
              .rating(dto.getRating())
              .difficulty(dto.getDifficulty())
              .careerLevel(dto.getCareerLevel())
              .interviewYearMonth(dto.getInterviewYearMonth())
              .interviewType(dto.getInterviewType())
              .interviewParticipants(dto.getInterviewParticipants())
              .hasFrequentQuestions(dto.getHasFrequentQuestions())
              .questionsAsked(dto.getQuestionsAsked())
              .interviewTip(dto.getInterviewTip())
              .result(dto.getResult())
              .status(0)
              .build();
    }

    interviewReviewRepository.save(review);
    return review.getId();

  }



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
