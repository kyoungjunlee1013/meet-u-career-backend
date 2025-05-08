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
    private Long id;
    private String jobId;
    private JobPostingInfo jobPosting;
    private CompanyInfo company;
    private int bookmarkCount;
    private int companyFollowCount;
    private int openJobPostingCount;
    private int interviewReviewCount;
    private boolean isCompanyFollowed;
    private boolean isBookmarked;
    private boolean isApplied;
    private ApplicantStats applicantStats;

    /**
     * 공고 정보
     */
    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class JobPostingInfo {
        private String title;
        private String industry;
        private String jobType;
        private String jobUrl;
        private String locationCode;
        private Integer experienceLevel;
        private String experienceLevelName;
        private Integer educationLevel;
        private String educationLevelName;
        private String salaryRange;
        private LocalDateTime postingDate;
        private LocalDateTime expirationDate;
        private Integer viewCount;
        private Integer applyCount;
        private String keyword;
        private String description;
    }

    /**
     * 기업 정보
     */
    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyInfo {
        private Long companyId;
        private String companyName;
        private String representativeName;
        private String industry;
        private String address;
        private String website;
        private String logoUrl;
        private LocalDate foundedDate;
        private Integer numEmployees;
        private Long revenue;
    }

    /**
     * 지원자 통계 정보 (경력, 학력, 연봉)
     */
    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ApplicantStats {
        private int totalApplicantCount;
        private ExperienceStats experienceStats;
        private EducationStats educationStats;
        private SalaryStats salaryStats;

        @Data @Builder @AllArgsConstructor @NoArgsConstructor
        public static class ExperienceStats {
            private int newApplicantCount;
            private int experiencedApplicantCount;
        }

        @Data @Builder @AllArgsConstructor @NoArgsConstructor
        public static class EducationStats {
            private int highSchoolCount;
            private int collegeCount;
            private int universityCount;
            private int masterCount;
            private int doctorCount;
            private int etcCount;
        }

        @Data @Builder @AllArgsConstructor @NoArgsConstructor
        public static class SalaryStats {
            private int below4000Count;
            private int range4000to6000Count;
            private int range6000to8000Count;
            private int above8000Count;
            private int negotiableCount;
        }
    }

    /**
     * 경력
     */
    private static String getExperienceLevelName(Integer code) {
        return switch (code) {
            case JobPosting.ExperienceCode.NEW -> "신입";
            case JobPosting.ExperienceCode.EXPERIENCED -> "경력";
            case JobPosting.ExperienceCode.BOTH -> "신입/경력";
            default -> "기타";
        };
    }

    /**
     * 학력
     */
    private static String getEducationLevelName(Integer code) {
        return switch (code) {
            case JobPosting.EducationLevel.ANY -> "학력무관";
            case JobPosting.EducationLevel.HIGH_SCHOOL -> "고등학교졸업";
            case JobPosting.EducationLevel.COLLEGE -> "대학졸업(2,3년)";
            case JobPosting.EducationLevel.UNIVERSITY -> "대학교졸업(4년)";
            case JobPosting.EducationLevel.MASTER -> "석사졸업";
            case JobPosting.EducationLevel.DOCTOR -> "박사졸업";
            case JobPosting.EducationLevel.HIGH_SCHOOL_OR_MORE -> "고등학교졸업이상";
            case JobPosting.EducationLevel.COLLEGE_OR_MORE -> "대학졸업(2,3년)이상";
            case JobPosting.EducationLevel.UNIVERSITY_OR_MORE -> "대학교졸업(4년)이상";
            case JobPosting.EducationLevel.MASTER_OR_MORE -> "석사졸업이상";
            default -> "기타";
        };
    }

    public static JobPostingDetailDTO from(
        JobPosting jobPosting,
        int bookmarkCount,
        int companyFollowCount,
        int openJobPostingCount,
        int interviewReviewCount,
        boolean isCompanyFollowed,
        boolean isBookmarked,
        boolean isApplied,
        int totalCount,
        ApplicantStats.ExperienceStats experienceStats,
        ApplicantStats.EducationStats educationStats,
        ApplicantStats.SalaryStats salaryStats
    ) {
        Company company = jobPosting.getCompany();

        return JobPostingDetailDTO.builder()
            .id(jobPosting.getId())
            .jobId(jobPosting.getJobId())
            .jobPosting(JobPostingInfo.builder()
                .title(jobPosting.getTitle())
                .industry(jobPosting.getIndustry())
                .jobType(jobPosting.getJobType())
                .jobUrl(jobPosting.getJobUrl())
                .locationCode(jobPosting.getLocation().getFullLocation())
                .experienceLevel(jobPosting.getExperienceLevel())
                .experienceLevelName(getExperienceLevelName(jobPosting.getExperienceLevel()))
                .educationLevel(jobPosting.getEducationLevel())
                .educationLevelName(getEducationLevelName(jobPosting.getEducationLevel()))
                .salaryRange(jobPosting.getSalaryRange())
                .postingDate(jobPosting.getPostingDate())
                .expirationDate(jobPosting.getExpirationDate())
                .viewCount(jobPosting.getViewCount())
                .applyCount(jobPosting.getApplyCount())
                .keyword(jobPosting.getKeyword())
                .description(jobPosting.getDescription())
                .build())
            .company(CompanyInfo.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .representativeName(company.getRepresentativeName())
                .industry(company.getIndustry())
                .address(company.getAddress())
                .website(company.getWebsite())
                .logoUrl(company.getLogoKey())
                .foundedDate(company.getFoundedDate())
                .numEmployees(company.getNumEmployees())
                .revenue(company.getRevenue())
                .build())
            .bookmarkCount(bookmarkCount)
            .companyFollowCount(companyFollowCount)
            .openJobPostingCount(openJobPostingCount)
            .interviewReviewCount(interviewReviewCount)
            .isCompanyFollowed(isCompanyFollowed)
            .isBookmarked(isBookmarked)
            .isApplied(isApplied)
            .applicantStats(ApplicantStats.builder()
                .totalApplicantCount(totalCount)
                .experienceStats(experienceStats)
                .educationStats(educationStats)
                .salaryStats(salaryStats)
                .build())
            .build();
    }
}
