package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationStats {
    private DashboardMetricDTO totalApplications;              // 전체 지원 수
    private DashboardMetricDTO acceptedApplications;           // 서류합격 수
    private DashboardMetricDTO rejectedApplications;           // 불합격 수
    private List<DailyApplicationStatsDTO> applicationTrends;  // 지원 추이
    private List<AgeGroupDTO> applicantAgeGroupChart;          // 연령 분포
    private List<TopCompanyJobPostingsDTO> top5Companies;      // 상위 5개 기업
    private List<JobCategoryCountDTO> jobCategoryChart;        // 카테고리 분포
    private List<JobCategoryCountDTO> jobCategoryPostings;     // 직무별 통계
    private List<ApplicationTimeStatDTO> applicationTimeStats; // 시간대별 통계
}
