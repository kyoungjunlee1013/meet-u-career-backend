package com.highfive.meetu.domain.job.business.controller;

import com.highfive.meetu.domain.job.business.dto.ApplicantResponseDTO;
import com.highfive.meetu.domain.job.business.dto.JobPostingResponseDTO;
import com.highfive.meetu.domain.job.business.dto.ResumeDetailResponseDTO;
import com.highfive.meetu.domain.job.business.service.ApplicantBusinessService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [기업회원] 지원자 관리 API 컨트롤러
 * - 기업이 등록한 채용공고에 대한 지원자 목록 조회
 * - 기업 소속 공고 목록 조회
 */
@RestController
@RequestMapping("/api/business/applicants")
@RequiredArgsConstructor
public class ApplicantBusinessController {
    private final ApplicantBusinessService applicantBusinessService;

    /**
     * 현재 로그인한 기업 계정이 등록한 채용공고 목록 조회 API
     *
     * @return 기업이 등록한 채용공고 리스트
     */
    @GetMapping
    public ResultData<List<JobPostingResponseDTO>> getMyCompanyJobPostings() {
        List<JobPostingResponseDTO> postings = applicantBusinessService.getMyCompanyJobPostings();
        return ResultData.success(postings.size(), postings);
    }

    /**ㄴ
     * 특정 채용공고에 지원한 지원자 목록 조회 API
     *
     * @param jobPostingId 채용공고 ID (PathVariable로 전달)
     * @return 해당 공고에 지원한 지원자 리스트
     */
    @GetMapping("/{jobPostingId}")
    public ResultData<List<ApplicantResponseDTO>> getApplicantsByJobPosting(@PathVariable Long jobPostingId) {
        List<ApplicantResponseDTO> applicants = applicantBusinessService.getApplicantsByJobPosting(jobPostingId);
        return ResultData.success(applicants.size(), applicants);
    }

    /**
     * 특정 지원서(applicationId)를 기반으로 제출된 이력서 상세 정보 조회 API
     *
     * @param applicationId 지원서 ID
     * @return 이력서 상세 정보 (ResumeDetailResponseDTO)
     */
    @GetMapping("/resume/{applicationId}")
    public ResultData<ResumeDetailResponseDTO> getResumeByApplicationId(@PathVariable Long applicationId) {
        ResumeDetailResponseDTO resume = applicantBusinessService.getResumeDetailByApplicationId(applicationId);
        return ResultData.success(1, resume);
    }
}
