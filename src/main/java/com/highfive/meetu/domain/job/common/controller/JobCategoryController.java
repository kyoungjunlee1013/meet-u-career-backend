package com.highfive.meetu.domain.job.common.controller;

import com.highfive.meetu.domain.job.common.dto.JobCategoryOptionDTO;
import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;
import com.highfive.meetu.domain.job.common.service.JobCategoryService;
import com.highfive.meetu.domain.job.common.service.LocationService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
@RequiredArgsConstructor
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    /**
     * 직무 자동완성 검색
     * @param keyword 입력된 키워드 (2글자 이상)
     */
    @GetMapping("/search")
    public ResultData<List<JobCategoryOptionDTO>> searchJobCategories(@RequestParam String keyword) {
        return jobCategoryService.searchJobCategories(keyword);
    }
}
