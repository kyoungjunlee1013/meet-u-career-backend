package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.entity.InterviewReview;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewCreateDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.job.common.repository.JobCategoryRepository;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 개인회원 면접 후기 서비스 (작성 처리)
 */
@Service
@RequiredArgsConstructor
public class InterviewReviewPersonalService {

  private final InterviewReviewRepository interviewReviewRepository;
  private final ProfileRepository profileRepository;
  private final CompanyRepository companyRepository;
  private final JobCategoryRepository jobCategoryRepository;
  private final ApplicationRepository applicationRepository;

  /**
   * 면접 후기 저장
   *
   * @param dto 후기 작성 요청 데이터
   * @param profileId 로그인된 사용자 프로필 ID
   * @return 저장된 후기 ID
   */
  @Transactional
  public Long createInterviewReview(InterviewReviewCreateDTO dto, Long profileId) {

    // 0. 이미 후기 등록 여부 확인
    if (interviewReviewRepository.existsByApplicationId(dto.getApplicationId())) {
      throw new BadRequestException("이미 해당 지원서에 대해 후기를 작성하셨습니다.");
    }

    // 1. 연관 엔티티 조회
    Profile profile = profileRepository.findById(profileId)
        .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

    Company company = companyRepository.findById(dto.getCompanyId())
        .orElseThrow(() -> new NotFoundException("회사를 찾을 수 없습니다."));

    JobCategory jobCategory = jobCategoryRepository.findById(dto.getJobCategoryId())
        .orElseThrow(() -> new NotFoundException("직무를 찾을 수 없습니다."));

    Application application = applicationRepository.findById(dto.getApplicationId())
        .orElseThrow(() -> new NotFoundException("지원 이력을 찾을 수 없습니다."));

    // jobCategoryId로부터 jobCode와 jobName 확인 (디버깅용 예시)
    String jobCode = jobCategory.getJobCode();
    String jobName = jobCategory.getJobName();

    // 2. InterviewReview 엔티티 생성
    InterviewReview review = InterviewReview.builder()
        .profile(profile)
        .company(company)
        .jobCategory(jobCategory)
        .application(application)
        .careerLevel(dto.getCareerLevel())
        .interviewYearMonth(dto.getInterviewYearMonth())
        .rating(dto.getRating())
        .difficulty(dto.getDifficulty())
        .interviewType(dto.getInterviewType())
        .interviewParticipants(dto.getInterviewParticipants())
        .hasFrequentQuestions(dto.getHasFrequentQuestions())
        .questionsAsked(dto.getQuestionsAsked())
        .interviewTip(dto.getInterviewTip())
        .result(dto.getResult())
        .status(0) // 기본 상태: 활성
        .build();

    // 3. 저장 및 ID 반환
    InterviewReview saved = interviewReviewRepository.save(review);
    return saved.getId();
  }


  /**
   * 특정 프로필이 작성한 면접 후기 목록 조회
   *
   * - profileId를 기준으로 작성된 후기들을 모두 가져옴
   * - 연관된 회사명, 직무명도 함께 조회하여 DTO로 변환
   * - 프론트에서 목록 화면에 사용할 수 있도록 구성됨
   *
   * @param profileId 로그인된 사용자 프로필 ID
   * @return InterviewReviewPersonalDTO 리스트
   */
  @Transactional(readOnly = true)
  public List<InterviewReviewPersonalDTO> findAllByProfileId(Long profileId) {

    // 1. 후기 목록 조회 (Profile 기준) - 연관 데이터는 지연 로딩
    List<InterviewReview> reviews = interviewReviewRepository.findAllByProfile_Id(profileId);

    // 2. Entity → DTO 변환
    return reviews.stream()
        .map(review -> InterviewReviewPersonalDTO.builder()
            .id(review.getId())  // 후기 ID
            .companyName(review.getCompany().getName())  // 회사명 (조회용)
            .jobCategoryName(review.getJobCategory().getJobName())  // 직무명 (조회용)
            .interviewYearMonth(review.getInterviewYearMonth())  // 면접 연월
            .rating(review.getRating())  // 전반적 평가
            .createdAt(review.getCreatedAt())  // 작성일시

            // 상세에서 사용할 정보
            .careerLevel(review.getCareerLevel())
            .difficulty(review.getDifficulty())
            .interviewType(review.getInterviewType())
            .interviewParticipants(review.getInterviewParticipants())
            .hasFrequentQuestions(review.getHasFrequentQuestions())
            .questionsAsked(review.getQuestionsAsked())
            .interviewTip(review.getInterviewTip())
            .result(review.getResult())

            // 연관 ID (프론트에서 수정 시 사용 가능)
            .applicationId(review.getApplication().getId())
            .companyId(review.getCompany().getId())
            .jobCategoryId(review.getJobCategory().getId())

            // 상태값 (0: 활성, 1: 삭제 요청 등)
            .status(review.getStatus())
            .build())
        .toList();
  }
}