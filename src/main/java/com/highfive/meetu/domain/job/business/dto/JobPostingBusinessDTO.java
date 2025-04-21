package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingBusinessDTO {

    // 기본 식별
    private Long id;

    // 작성 관련
    private Long companyId;
    private String companyName;  // 회사 이름 (예: "하이파이브")
    private Long businessAccountId;
    private String title;
    private String jobUrl;
    private String industry;
    private String jobType;
    private String locationCode;
    private String locationName;             // fullLocation (조회용)
    private Integer experienceLevel;
    private Integer educationLevel;
    private Integer salaryCode;
    private String salaryRange;
    private LocalDateTime postingDate;
    private LocalDateTime openingDate;
    private LocalDateTime expirationDate;
    private Integer closeType;
    private String keyword;
    private Integer templateType;
    private String description;
    private List<Long> jobCategoryIds;

    // 조회/상태용
    private Integer status;
    private String statusLabel;              // ✅ 프론트와 공유할 경우
    private Integer viewCount;
    private Integer applyCount;
    private String companyLogoKey;           // ✅ 조회에만 필요 (선택)


    public static JobPostingBusinessDTO fromEntity(JobPosting jobPosting, List<Long> jobCategoryIds) {
        return JobPostingBusinessDTO.builder()
                .id(jobPosting.getId())
                .companyId(jobPosting.getCompany().getId())
                .companyName(jobPosting.getCompany().getName())  // 회사 이름 매핑
                .businessAccountId(jobPosting.getBusinessAccount() != null ? jobPosting.getBusinessAccount().getId() : null)
                .title(jobPosting.getTitle())
                .jobUrl(jobPosting.getJobUrl())
                .industry(jobPosting.getIndustry())
                .jobType(jobPosting.getJobType())
                .locationCode(jobPosting.getLocation().getLocationCode())
                .locationName(jobPosting.getLocation().getFullLocation()) // 추가
                .experienceLevel(jobPosting.getExperienceLevel())
                .educationLevel(jobPosting.getEducationLevel())
                .salaryCode(jobPosting.getSalaryCode())
                .salaryRange(jobPosting.getSalaryRange())
                .postingDate(jobPosting.getPostingDate())
                .openingDate(jobPosting.getOpeningDate())
                .expirationDate(jobPosting.getExpirationDate())
                .closeType(jobPosting.getCloseType())
                .keyword(jobPosting.getKeyword())
                .templateType(jobPosting.getTemplateType())
                .description(jobPosting.getDescription())
                .status(jobPosting.getStatus())
                .statusLabel(convertStatus(jobPosting.getStatus())) // 선택
                .viewCount(jobPosting.getViewCount())
                .applyCount(jobPosting.getApplyCount())
                .jobCategoryIds(jobCategoryIds)
                .companyLogoKey(jobPosting.getCompany().getLogoKey()) // 회사 로고 (선택)
                .build();
    }

    private static String convertStatus(Integer status) {
        return switch (status) {
            case 0 -> "임시저장";
            case 1 -> "승인대기";
            case 2 -> "반려됨";
            case 3 -> "승인됨(게시 전)";
            case 4 -> "게시중";
            case 5 -> "마감됨";
            default -> "알 수 없음";
        };
    }



}
