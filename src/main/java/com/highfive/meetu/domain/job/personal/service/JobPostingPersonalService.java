package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.application.common.repository.InterviewReviewRepository;
import com.highfive.meetu.domain.company.common.repository.CompanyFollowRepository;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.*;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDetailDTO;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingPersonalService {

    private final JobPostingRepository jobPostingRepository;
    private final JobPostingCustomRepository jobPostingCustomRepository;
    private final LocationRepository locationRepository;
    private final CompanyFollowRepository companyFollowRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ApplicationRepository applicationRepository;
    private final ProfileRepository profileRepository;
    private final InterviewReviewRepository interviewReviewRepository;
    private final ApplicationQueryPersonalRepository applicationQueryRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private static final String JOB_VIEW_PREFIX = "job_view:";

    /**
     * 통합 검색 (필터 + 키워드 + 정렬)
     */
    @Transactional(readOnly = true)
    public Page<JobPostingDTO> searchJobPostings(
            List<String> industry,
            Integer experienceLevel,
            Integer educationLevel,
            List<String> locationCode,
            String keyword,
            String sort,
            Pageable pageable
    ) {
        Page<JobPosting> jobPostings = jobPostingCustomRepository.searchByFilters(
                industry, experienceLevel, educationLevel, locationCode, keyword, sort, pageable
        );

        return jobPostings.map(JobPostingDTO::fromEntity);
    }



    /**
     * 전체 목록(기본: 최신순) 조회 (페이지네이션 적용)
     */
    @Transactional(readOnly = true)
    public Page<JobPostingDTO> getJobPostingList(Pageable pageable) {
        return searchJobPostings(
                null, null, null, null,
                null, "newest",
                pageable
        );
    }

    /**
     * 단일 상세 조회
     */
    @Transactional(readOnly = true)
    public JobPostingDTO getJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채용공고입니다."));
        return JobPostingDTO.fromEntity(jobPosting);
    }

    /**
     * 시/도 선택 시 시군구까지 확장
     */
    private List<String> expandLocationCodes(List<String> inputCodes) {
        if (inputCodes == null || inputCodes.isEmpty()) return null;

        List<String> expanded = new ArrayList<>();

        for (String code : inputCodes) {
            locationRepository.findByLocationCode(code).ifPresent(provinceLoc -> {
                expanded.add(code); // 본인도 포함

                // 해당 시/도에 속한 시/군/구 추가
                List<String> childCodes = locationRepository.findByProvince(provinceLoc.getProvince())
                        .stream()
                        .map(loc -> loc.getLocationCode())
                        .filter(c -> !c.equals(code)) // 본인은 중복되므로 제거
                        .toList();

                expanded.addAll(childCodes);
            });
        }

        return expanded;
    }

    /**
     * 채용 공고 상세 조회
     * - 로그인 여부에 따라 스크랩 여부, 지원 여부, 관심기업 여부, 조회수 증가 포함
     *
     * @param jobPostingId 조회할 공고 ID
     * @return ResultData로 래핑된 JobPostingDetailDTO
     */
    @Transactional
    public ResultData<JobPostingDetailDTO> getJobPostingDetails(Long jobPostingId, HttpServletRequest request) {
        // 1. 공고 기본 정보 조회 (활성화된 공고만)
        JobPosting jobPosting = jobPostingRepository.findByIdAndStatus(jobPostingId, JobPosting.Status.ACTIVE)
            .orElseThrow(() -> new NotFoundException("존재하지 않거나 비활성화된 공고입니다."));

        // 2. 로그인 여부에 따라 개인 프로필 및 상태 체크
        boolean isAuthenticated = SecurityUtil.isAuthenticated();
        boolean isCompanyFollowed = false;
        boolean isBookmarked = false;
        boolean isApplied = false;

        Profile profile = null;

        if (isAuthenticated) {
            Long accountId = SecurityUtil.getAccountId();
            String redisKey = buildRedisKeyForUser(accountId, jobPostingId);

            // 조회수 증가
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                jobPosting.setViewCount(jobPosting.getViewCount() + 1);
                redisTemplate.opsForValue().set(redisKey, "true", Duration.ofHours(24));
            }

            profile = profileRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("사용자 프로필을 찾을 수 없습니다."));

            isCompanyFollowed = companyFollowRepository.existsByProfileAndCompany(profile, jobPosting.getCompany());
            isBookmarked = bookmarkRepository.existsByProfileAndJobPosting(profile, jobPosting);
            isApplied = applicationRepository.existsByProfileAndJobPosting(profile, jobPosting);
        }

        // 3. 지원자 통계 정보 조회
        int totalApplicants = applicationQueryRepository.countApplicantsByJobPostingId(jobPostingId);
        int newApplicants = applicationQueryRepository.countNewApplicants(jobPostingId);
        int experiencedApplicants = applicationQueryRepository.countExperiencedApplicants(jobPostingId);

        JobPostingDetailDTO.ApplicantStats.EducationStats educationStats = JobPostingDetailDTO.ApplicantStats.EducationStats.builder()
            .highSchoolCount(applicationQueryRepository.countApplicantsByEducationLevel(jobPostingId, 1))
            .collegeCount(applicationQueryRepository.countApplicantsByEducationLevel(jobPostingId, 2))
            .universityCount(applicationQueryRepository.countApplicantsByEducationLevel(jobPostingId, 3))
            .masterCount(applicationQueryRepository.countApplicantsByEducationLevel(jobPostingId, 4))
            .doctorCount(applicationQueryRepository.countApplicantsByEducationLevel(jobPostingId, 5))
            .etcCount(0) // 기타 학력 카운트는 필요 시 추가 가능
            .build();

        JobPostingDetailDTO.ApplicantStats.SalaryStats salaryStats = JobPostingDetailDTO.ApplicantStats.SalaryStats.builder()
            .below4000Count(applicationQueryRepository.countSalaryBelow4000(jobPostingId))
            .range4000to6000Count(applicationQueryRepository.countSalaryRange4000to6000(jobPostingId))
            .range6000to8000Count(applicationQueryRepository.countSalaryRange6000to8000(jobPostingId))
            .above8000Count(applicationQueryRepository.countSalaryAbove8000(jobPostingId))
            .negotiableCount(applicationQueryRepository.countNegotiableSalary(jobPostingId))
            .build();

        // 4. 기업/공고 관련 통계 조회
        int bookmarkCount = bookmarkRepository.countByJobPostingId(jobPostingId); // 공고 스크랩 수
        int companyFollowCount = companyFollowRepository.countByCompanyId(jobPosting.getCompany().getId()); // 회사 팔로우 수
        int openJobPostingCount = jobPostingRepository.countByCompanyIdAndStatus(jobPosting.getCompany().getId(), JobPosting.Status.ACTIVE); // 현재 열려있는 공고 수
        int interviewReviewCount = interviewReviewRepository.countByCompanyId(jobPosting.getCompany().getId()); // 인터뷰 후기 수

        // 5. DTO 변환 및 결과 반환
        JobPostingDetailDTO dto = JobPostingDetailDTO.from(
            jobPosting,
            bookmarkCount,
            companyFollowCount,
            openJobPostingCount,
            interviewReviewCount,
            isCompanyFollowed,
            isBookmarked,
            isApplied,
            totalApplicants,
            JobPostingDetailDTO.ApplicantStats.ExperienceStats.builder()
                .newApplicantCount(newApplicants)
                .experiencedApplicantCount(experiencedApplicants)
                .build(),
            educationStats,
            salaryStats
        );

        return ResultData.success(1, dto);
    }

    /**
     * Redis 조회수 중복 방지를 위한 고유 키 생성 메서드
     *
     * - 하루 동안 동일한 사용자(accountId)가 동일한 공고(jobPostingId)를 여러 번 조회해도
     *   조회수는 1회만 증가하도록 하기 위해 고유 키를 생성함.
     * - Redis에 이 키가 존재하면 조회수 증가를 방지하고,
     *   존재하지 않으면 조회수 증가 후 24시간 TTL을 부여함.
     *
     * @param accountId 로그인한 사용자 계정 ID
     * @param jobPostingId 조회 대상 채용 공고 ID
     * @return Redis 저장용 고유 키 (예: "job_view:user:3:17")
     */
    private String buildRedisKeyForUser(Long accountId, Long jobPostingId) {
        return JOB_VIEW_PREFIX + "user:" + accountId + ":" + jobPostingId;
    }
}
