package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.business.dto.*;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import com.highfive.meetu.domain.resume.common.repository.ResumeContentRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [기업회원] 채용 지원자 및 공고 관리 서비스
 * - 특정 채용공고에 대한 지원자 조회
 * - 로그인한 기업회원이 등록한 채용공고 목록 조회
 */
@Service
@RequiredArgsConstructor
public class ApplicantBusinessService {

    private final ApplicationRepository applicationRepository;
    private final AccountRepository accountRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ResumeContentRepository resumeContentRepository;

    /**
     * 현재 로그인한 기업회원이 소속된 기업의 "활성 + 마감되지 않은" 채용공고 목록을 반환
     *
     * 조건:
     * - status = 2 (활성 상태)
     * - expirationDate > 현재 시간 (아직 마감되지 않음)
     *
     * @return 해당 기업의 유효한 채용공고 리스트 (JobPostingResponseDTO 리스트)
     */
    public List<JobPostingResponseDTO> getMyCompanyJobPostings() {
        Long accountId = SecurityUtil.getAccountId(); // 로그인한 사용자 ID

        // 계정 정보 조회
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));

        // 계정에 연결된 기업 조회
        Company company = account.getCompany();
        if (company == null) {
            throw new NotFoundException("소속된 기업이 없습니다.");
        }

        // 해당 기업의 유효한 채용공고 조회 후 DTO로 변환
        return jobPostingRepository.findAllActiveAndNotExpiredByCompanyId(company.getId())
            .stream()
            .map(JobPostingResponseDTO::fromEntity)
            .toList();
    }

    /**
     * 특정 채용공고에 지원한 지원자 목록을 반환
     *
     * @param jobPostingId 채용공고 ID
     * @return 지원자 목록 (ApplicantResponseDTO 리스트)
     */
    public List<ApplicantResponseDTO> getApplicantsByJobPosting(Long jobPostingId) {
        return applicationRepository.findAllByJobPostingId(jobPostingId)
            .stream()
            .map(ApplicantResponseDTO::fromEntity)
            .toList();
    }

    /**
     * 특정 지원서(applicationId)를 기반으로 제출된 이력서의 상세 정보를 조회하는 메서드
     *
     * [조회 항목]
     * - 기본 이력서 정보: 제목, 개요, 파일, 링크 등
     * - 학력 정보 (sectionType = 0)
     * - 경력 정보 (sectionType = 1)
     * - 자격증 정보 (sectionType = 2)
     * - 프로젝트 정보 (sectionType = 3)
     * - 언어 능력 정보 (sectionTitle = "언어"인 항목 추론)
     * - 기술 스택 (profile.skills 문자열을 쉼표로 분할)
     *
     * [출처 테이블]
     * - application → resume → resumeContent, profile
     *
     * [제약 조건]
     * - resumeContent는 sectionType으로 항목 구분
     * - skills는 Profile의 skills 필드에서 문자열 분할
     * - 엔티티 수정 없이 정규화된 DTO 형태로 가공 반환
     *
     * @param applicationId 지원서 ID
     * @return ResumeDetailResponseDTO 이력서 상세 응답 DTO
     */
    public ResumeDetailResponseDTO getResumeDetailByApplicationId(Long applicationId) {
        // 1. 지원서 조회 → 이력서, 프로필 추출
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));
        Resume resume = application.getResume();
        Profile profile = resume.getProfile();

        // 2. resumeContent 항목 조회
        List<ResumeContent> contents = resumeContentRepository.findByResumeIdOrderByContentOrderAsc(resume.getId());

        // 3. 항목별 분리
        List<EducationDTO> educations = new ArrayList<>();
        List<ExperienceDTO> experiences = new ArrayList<>();
        List<ProjectDTO> projects = new ArrayList<>();
        List<CertificateDTO> certificates = new ArrayList<>();
        List<LanguageDTO> languages = new ArrayList<>();

        for (ResumeContent content : contents) {
            switch (content.getSectionType()) {
                case 0 -> educations.add(EducationDTO.from(content));
                case 1 -> experiences.add(ExperienceDTO.from(content));
                case 2 -> certificates.add(CertificateDTO.from(content));
                case 3 -> projects.add(ProjectDTO.from(content));
            }

            // 언어 항목 추론용 예시 (sectionTitle = "언어", organization = "영어", title = "비즈니스 회화")
            if ("언어".equalsIgnoreCase(content.getSectionTitle())) {
                languages.add(LanguageDTO.from(content.getOrganization(), content.getTitle()));
            }
        }

        // 4. 기술 스택 (쉼표 기반 분할)
        List<String> skills = Optional.ofNullable(profile.getSkills())
            .map(s -> Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList()))
            .orElse(List.of());

        return ResumeDetailResponseDTO.fromEntity(
            resume,
            educations,
            experiences,
            projects,
            skills,
            languages,
            certificates
        );
    }

    /**
     * 특정 채용공고에 대한 지원자 통계를 반환합니다.
     *
     * 통계 항목:
     * - 총 지원자 수
     * - 서류 검토 중 수 (status = APPLIED)
     * - 서류 합격 수 (status = DOCUMENT_PASSED)
     * - 서류 불합격 수 (status = DOCUMENT_REJECTED)
     * - 면접 완료 수 (status = INTERVIEW_COMPLETED)
     *
     * @param jobPostingId 채용공고 ID
     * @return 지원자 통계 정보를 담은 ApplicantStatsDTO
     */
    public ApplicantStatsDTO getApplicantStats(Long jobPostingId) {
        // 총 지원자 수
        int total = applicationRepository.countByJobPostingId(jobPostingId);
        
        // 서류 검토
        int reviewing = applicationRepository.countByJobPostingIdAndStatus(jobPostingId, Application.Status.APPLIED);
        
        // 서류 합격
        int passed = applicationRepository.countByJobPostingIdAndStatus(jobPostingId, Application.Status.DOCUMENT_PASSED);
        
        // 서류 탈락
        int failed = applicationRepository.countByJobPostingIdAndStatus(jobPostingId, Application.Status.DOCUMENT_REJECTED);
        
        // 면접 완료
        int interviewed = applicationRepository.countByJobPostingIdAndStatus(jobPostingId, Application.Status.INTERVIEW_COMPLETED);

        return new ApplicantStatsDTO(total, reviewing, passed, failed, interviewed);
    }

    /**
     * 지원서 상태(status)를 업데이트하는 메서드
     *
     * @param applicationId 지원서 ID
     * @param status 변경할 상태값 (0: 서류검토중, 1: 서류합격, 2: 서류불합격, 3: 면접완료)
     * @throws IllegalArgumentException 지원서가 존재하지 않는 경우 예외 발생
     */
    @Transactional
    public void updateStatus(Long applicationId, Integer status) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));
        application.setStatus(status);
    }
}
