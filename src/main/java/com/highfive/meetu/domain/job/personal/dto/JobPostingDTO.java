package com.highfive.meetu.domain.job.personal.dto;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingDTO {
    private Long id;
    private String jobId;
    private Long companyId;
    private Long businessAccountId; // Account의 id를 저장
    private String title;
    private String jobUrl;
    private String industry;
    private String jobType;
    private String locationCode;
    private Integer experienceLevel;
    private Integer educationLevel;
    private Integer salaryCode;
    private String salaryRange;
    private LocalDateTime postingDate;
    private LocalDateTime openingDate;
    private LocalDateTime expirationDate;
    private Integer closeType;
    private Integer viewCount;
    private Integer applyCount;
    private String keyword;
    private String templateType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer status;

    private LocationDTO location;

    // 엔티티 -> DTO 변환 메서드
    public static JobPostingDTO fromEntity(JobPosting job) {
        return JobPostingDTO.builder()
                .id(job.getId())
                .jobId(job.getJobId())
                // company가 null 체크되어 있음
                .companyId(job.getCompany() != null ? job.getCompany().getId() : null)
                // businessAccount에 null 체크 추가!
                .businessAccountId(job.getBusinessAccount() != null ? job.getBusinessAccount().getId() : null)
                .title(job.getTitle())
                .jobUrl(job.getJobUrl())
                .industry(job.getIndustry())
                .jobType(job.getJobType())
                // location 연관 객체의 경우 null 체크
                .location(job.getLocation() != null ? LocationDTO.fromEntity(job.getLocation()) : null)
                .experienceLevel(job.getExperienceLevel())
                .educationLevel(job.getEducationLevel())
                .salaryCode(job.getSalaryCode())
                .salaryRange(job.getSalaryRange())
                .postingDate(job.getPostingDate())
                .openingDate(job.getOpeningDate())
                .expirationDate(job.getExpirationDate())
                .closeType(job.getCloseType())
                .viewCount(job.getViewCount())
                .applyCount(job.getApplyCount())
                .keyword(job.getKeyword())
                .templateType(job.getTemplateType() != null ? job.getTemplateType().toString() : null)
                .description(job.getDescription())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .status(job.getStatus())
                .build();
    }
}