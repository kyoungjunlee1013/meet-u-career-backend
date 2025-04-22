package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobBusinessService {

    private final JobPostingRepository jobPostingRepository;

    // 특정 기업의 전체 공고 목록 조회
    public ResultData<List<JobPostingBusinessDTO>> getJobPostingsByCompany(Long companyId) {
        List<JobPosting> postings = jobPostingRepository.findByCompanyId(companyId);

        List<JobPostingBusinessDTO> dtoList = postings.stream()
                .map(posting -> {
                    List<Long> categoryIds = posting.getJobPostingJobCategoryList().stream()
                            .map(jc -> jc.getJobCategory().getId())
                            .toList();

                    return JobPostingBusinessDTO.fromEntity(posting, categoryIds);
                })
                .toList();

        return ResultData.success(dtoList.size(), dtoList);
    }

    // 공고 상세 조회 (결제 등에서 사용)
    public ResultData<JobPostingBusinessDTO> getJobPostingById(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("공고를 찾을 수 없습니다."));

        // 상세도 목록과 동일하게 categoryIds 추출
        List<Long> categoryIds = jobPosting.getJobPostingJobCategoryList().stream()
                .map(jc -> jc.getJobCategory().getId())
                .toList();

        JobPostingBusinessDTO dto = JobPostingBusinessDTO.fromEntity(jobPosting, categoryIds);
        return ResultData.success(1, dto);
    }
}
