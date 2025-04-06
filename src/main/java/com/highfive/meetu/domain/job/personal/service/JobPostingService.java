package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDetailDTO;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    @Transactional(readOnly = true)
    public ResultData<JobPostingDetailDTO> getJobPostingDetail(Long id) {
        return jobPostingRepository.findByIdAndStatus(id, JobPosting.Status.ACTIVE)
            .map(JobPostingDetailDTO::from)
            .map(dto -> ResultData.success(1, dto))
            .orElse(ResultData.fail("존재하지 않거나 비활성화된 공고입니다."));
    }
}

