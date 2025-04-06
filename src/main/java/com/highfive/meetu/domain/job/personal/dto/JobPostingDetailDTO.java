package com.highfive.meetu.domain.job.personal.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

import java.time.LocalDate;
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
    private String jobUrl;
    private String locationCode;
    private Integer experienceLevel;
    private Integer educationLevel;
    private String salaryRange;
    private LocalDateTime postingDate;
    private LocalDateTime expirationDate;
    private Integer viewCount;
    private Integer applyCount;
    private String keyword;
    private String description;

    // 기업 정보
    private Long companyId;
    private String companyName;
    private String companyIndustry;
    private String companyAddress;
    private String companyWebsite;
    private String companyLogoUrl;
    private LocalDate foundedDate;
    private Integer numEmployees;
    private Long revenue;

    public static JobPostingDetailDTO from(JobPosting jobPosting) {
        Company company = jobPosting.getCompany();
        return JobPostingDetailDTO.builder()
            .id(jobPosting.getId())
            .title(jobPosting.getTitle())
            .jobUrl(jobPosting.getJobUrl())
            .industry(jobPosting.getIndustry())
            .jobType(jobPosting.getJobType())
            .locationCode(jobPosting.getLocation().getFullLocation())
            .experienceLevel(jobPosting.getExperienceLevel())
            .educationLevel(jobPosting.getEducationLevel())
            .salaryRange(jobPosting.getSalaryRange())
            .postingDate(jobPosting.getPostingDate())
            .expirationDate(jobPosting.getExpirationDate())
            .viewCount(jobPosting.getViewCount())
            .applyCount(jobPosting.getApplyCount())
            .keyword(jobPosting.getKeyword())
            .description(jobPosting.getDescription())
            .companyId(company.getId())
            .companyName(company.getName())
            .companyIndustry(company.getIndustry())
            .companyAddress(company.getAddress())
            .companyWebsite(company.getWebsite())
            .companyLogoUrl(company.getLogoUrl())
            .foundedDate(company.getFoundedDate())
            .numEmployees(company.getNumEmployees())
            .revenue(company.getRevenue())
            .build();
    }
}
