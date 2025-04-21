package com.highfive.meetu.domain.job.common.service;

import com.highfive.meetu.domain.job.common.dto.IndustryOptionDTO;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    /**
     * 산업 분야 자동완성 검색
     * @param keyword 사용자가 입력한 키워드
     * @return 산업 분야 목록
     */
    @Transactional(readOnly = true)
    public ResultData<List<IndustryOptionDTO>> searchIndustries(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return ResultData.fail("키워드는 최소 2글자 이상 입력해주세요.");
        }

        List<String> industries = jobPostingRepository.findDistinctIndustriesByKeyword(keyword.trim());

        List<IndustryOptionDTO> results = industries.stream()
                .map(industry -> IndustryOptionDTO.builder()
                        .value(industry)
                        .label(industry)
                        .build())
                .collect(Collectors.toList());

        return ResultData.success(results.size(), results);
    }
}
