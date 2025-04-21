package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.domain.job.personal.service.JobPostingService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 개인 회원용 채용공고 API 컨트롤러
 */
@RestController
@RequestMapping("/api/personal/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    /**
     * 채용공고 필터 조회
     * 프론트에서 전달된 조건에 따라 필터링된 공고 리스트 반환
     * 예: /api/personal/job/filter?jobType=백엔드&experienceLevel=1
     */
    @GetMapping("/filter")
    public ResultData<List<JobPostingDTO>> filterJobPostings(
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Integer experienceLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String locationCode
    ) {
        List<JobPostingDTO> filtered = jobPostingService
                .filterJobPostings(jobType, experienceLevel, educationLevel, locationCode);
        return ResultData.success(filtered.size(), filtered);
    }

    /**
     * 전체 채용공고 목록 조회 (최신순)
     */
    @GetMapping("/list")
    public ResultData<List<JobPostingDTO>> getJobPostingList() {
        List<JobPostingDTO> jobPostingList = jobPostingService.getJobPostingList();
        return ResultData.success(jobPostingList.size(), jobPostingList);
    }

    /**
     * 단일 채용공고 상세 조회
     */
    @GetMapping("/view/{jobPostingId}")
    public ResultData<JobPostingDTO> getJobPostingDetail(
            @PathVariable Long jobPostingId
    ) {
        JobPostingDTO jobPostingDetail = jobPostingService.getJobPostingDetail(jobPostingId);
        return ResultData.success(1, jobPostingDetail);
    }
}
