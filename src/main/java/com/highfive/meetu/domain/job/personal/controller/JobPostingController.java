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
     * 전체/필터/검색/정렬 통합 엔드포인트
     *
     * - industry           : 산업(직무)
     * - experienceLevel    : 경력 레벨
     * - educationLevel     : 학력 레벨
     * - locationCode       : 지역 코드
     * - keyword            : 키워드 포함 검색
     * - sort               : newest|popular|recommended
     *
     * 예) /api/personal/job/list?industry=개발&experienceLevel=1&keyword=Java&sort=popular
     */
    @GetMapping("/list")
    public ResultData<List<JobPostingDTO>> list(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) Integer experienceLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        List<JobPostingDTO> dtos = jobPostingService.searchJobPostings(
                industry, experienceLevel, educationLevel, locationCode, keyword, sort
        );
        return ResultData.success(dtos.size(), dtos);
    }

    /**
     * 단일 채용공고 상세 조회
     */
    @GetMapping("/view/{jobPostingId}")
    public ResultData<JobPostingDTO> view(@PathVariable Long jobPostingId) {
        JobPostingDTO dto = jobPostingService.getJobPostingDetail(jobPostingId);
        return ResultData.success(1, dto);
    }
}
