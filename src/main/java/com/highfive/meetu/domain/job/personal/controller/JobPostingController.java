package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.personal.dto.JobPostingDTO;
import com.highfive.meetu.domain.job.personal.service.JobPostingService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/job")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    /**
     * 채용공고 필터 API
     * 프론트에서 전달된 필터 파라미터에 따라 채용공고 목록을 조회합니다.
     *
     * @param jobType         채용 유형 (선택 사항)
     * @param experienceLevel 경력 (선택 사항)
     * @param educationLevel  학력 (선택 사항)
     * @param locationCode    지역 코드 (선택 사항)
     * @return 필터링된 채용공고 DTO 리스트
     */
    @GetMapping("/filter")
    public ResultData<List<JobPostingDTO>> filterJobPostings(
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Integer experienceLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String locationCode
    ) {
        List<JobPostingDTO> filtered = jobPostingService.filterJobPostings(jobType, experienceLevel, educationLevel, locationCode);
        return ResultData.success(filtered.size(), filtered);
    }

    // 기존 목록, 상세 조회 API (필요 시 함께 구현)
    @GetMapping("/list")
    public ResultData<List<JobPostingDTO>> getJobPostingList() {
        List<JobPostingDTO> jobPostingList = jobPostingService.getJobPostingList();
        return ResultData.success(jobPostingList.size(), jobPostingList);
    }

    @GetMapping("/view/{jobPostingId}")
    public ResultData<JobPostingDTO> getJobPostingDetail(@PathVariable Long jobPostingId) {
        JobPostingDTO jobPostingDetail = jobPostingService.getJobPostingDetail(jobPostingId);
        return ResultData.success(1, jobPostingDetail);
    }
}