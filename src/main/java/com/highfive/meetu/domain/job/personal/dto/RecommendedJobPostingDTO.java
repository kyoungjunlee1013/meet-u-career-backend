package com.highfive.meetu.domain.job.personal.dto;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedJobPostingDTO {
    private Long id;
    private String title;
    private String industry;
    private String jobType;
    private String salaryRange;

    public static RecommendedJobPostingDTO fromEntity(JobPosting jobPosting) {
        return RecommendedJobPostingDTO.builder()
            .id(jobPosting.getId())
            .title(jobPosting.getTitle())
            .industry(jobPosting.getIndustry())
            .jobType(jobPosting.getJobType())
            .salaryRange(jobPosting.getSalaryRange())
            .build();
    }
}
