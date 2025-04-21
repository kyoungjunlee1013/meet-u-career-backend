package com.highfive.meetu.domain.job.business.controller;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.business.service.JobBusinessService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/business/job")
@RequiredArgsConstructor
public class JobBusinessController {

    private final JobBusinessService jobBusinessService;

    /**
     * 특정 기업의 전체 공고 목록 조회
     * 추후 SecurityUtil.getCompanyId()로 대체 예정
     */
    @GetMapping("/my-list")
    public ResultData<List<JobPostingBusinessDTO>> getMyJobPostings() {
        Long companyId = 1L;  // SecurityUtil.getCompanyId()로 대체
        return jobBusinessService.getJobPostingsByCompany(companyId);
    }
}
