package com.highfive.meetu.domain.job.personal.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingDetailDTO {
    // 공고 정보
    private Long id;
    private String title;
    private String industry;
    private String jobType;
    private String locationCode;
    private String salaryRange;
    private LocalDateTime postingDate;
    private LocalDateTime expirationDate;

    // 기업 정보
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String companyIndustry;
    private String companyLogoUrl;

    public static JobPostingDetailDTO from(JobPosting entity) {
        Company company = entity.getCompany();

        return JobPostingDetailDTO.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .industry(entity.getIndustry())
            .jobType(entity.getJobType())
            .locationCode(entity.getLocation().getLocationCode())
            .salaryRange(entity.getSalaryRange())
            .postingDate(entity.getPostingDate())
            .expirationDate(entity.getExpirationDate())
            .companyId(company.getId())
            .companyName(company.getName())
            .companyAddress(company.getAddress())
            .companyIndustry(company.getIndustry())
            .companyLogoUrl(company.getLogoUrl())
            .build();
    }
}
