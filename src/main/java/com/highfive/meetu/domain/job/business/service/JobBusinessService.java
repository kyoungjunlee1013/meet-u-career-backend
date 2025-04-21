package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobBusinessService {

    private final JobPostingRepository jobPostingRepository;

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
}
