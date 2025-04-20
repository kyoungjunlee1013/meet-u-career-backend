package com.highfive.meetu.domain.job.common.service;

import com.highfive.meetu.domain.job.common.dto.JobCategoryOptionDTO;
import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;
import com.highfive.meetu.domain.job.common.repository.JobCategoryRepository;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    /**
     * 직무 카테고리 자동완성 검색
     * @param keyword 사용자가 입력한 키워드
     * @return 직무 목록
     */
    public ResultData<List<JobCategoryOptionDTO>> searchJobCategories(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            throw new BadRequestException("검색어는 2글자 이상 입력해야 합니다.");
        }

        List<JobCategoryOptionDTO> results = jobCategoryRepository.searchByKeyword(keyword.trim());

        return ResultData.success(results.size(), results);
    }
}
