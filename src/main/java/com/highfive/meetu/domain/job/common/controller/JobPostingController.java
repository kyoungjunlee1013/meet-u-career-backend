package com.highfive.meetu.domain.job.common.controller;

import com.highfive.meetu.domain.job.common.dto.IndustryOptionDTO;
import com.highfive.meetu.domain.job.common.service.JobPostingService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    /**
     * 산업 분야 자동완성 검색
     * @param keyword 입력된 키워드 (2글자 이상)
     */
    @GetMapping("/search")
    public ResultData<List<IndustryOptionDTO>> searchIndustries(@RequestParam String keyword) {
        return jobPostingService.searchIndustries(keyword);
    }
}
