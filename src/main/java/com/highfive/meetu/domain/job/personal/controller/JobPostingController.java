package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.personal.dto.JobPostingDetailDTO;
import com.highfive.meetu.domain.job.personal.service.JobPostingService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobpostings")
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @GetMapping("/{jobpostingId}")
    public ResultData<JobPostingDetailDTO> getJobPostingDetail(@PathVariable Long jobpostingId) {
        JobPostingDetailDTO detail = jobPostingService.getJobPostingDetail(jobpostingId);
        return ResultData.success(1, detail);
    }
}
