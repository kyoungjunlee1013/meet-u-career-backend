package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.business.dto.ApplicantResponseDTO;
import com.highfive.meetu.domain.job.business.dto.JobPostingResponseDTO;
import com.highfive.meetu.domain.job.business.dto.ResumeDetailResponseDTO;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
            .map(ApplicantResponseDTO::fromEntity) // Entity → DTO 변환
            .toList();
    }

    /**
     * 지원서 ID(applicationId)를 기반으로 지원자가 제출한 이력서 상세 정보를 조회하는 서비스 메서드
     *
     * @param applicationId 지원서 ID (지원 정보의 고유 식별자)
     * @return 이력서 상세 정보 DTO (ResumeDetailResponseDTO)
     * @throws NotFoundException 지원서 또는 이력서가 존재하지 않을 경우 발생
     */
    public ResumeDetailResponseDTO getResumeDetailByApplicationId(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new NotFoundException("지원 정보를 찾을 수 없습니다."));

        Resume resume = application.getResume();
        if (resume == null) {
            throw new NotFoundException("해당 지원자의 이력서가 없습니다.");
        }

        return ResumeDetailResponseDTO.fromEntity(resume);
    }
}
