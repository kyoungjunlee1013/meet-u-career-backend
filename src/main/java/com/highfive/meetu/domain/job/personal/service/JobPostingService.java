package com.highfive.meetu.domain.job.personal.service;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.personal.dto.JobPostingDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public JobPostingDetailDTO getJobPostingDetail(Long id) {
        JobPosting jobPosting = jobPostingRepository.findByIdAndStatus(id, JobPosting.Status.ACTIVE)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 비활성화된 공고입니다."));

        return JobPostingDetailDTO.from(jobPosting);
    }
}

