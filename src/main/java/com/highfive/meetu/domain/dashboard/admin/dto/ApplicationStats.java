package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationStats {
    private DashboardMetricDTO totalApplications;              // 전체 지원 수
    private DashboardMetricDTO inProgressApplications;         // 진행중인 지원 수
    private DashboardMetricDTO acceptedApplications;           // 합격 수
    private DashboardMetricDTO rejectedApplications;           // 불합격 수
    private List<DashboardMetricDTO> applicationTrends;        // 지원 추이
    private List<ConversionRateDTO> conversionRates;           // 전환율
    private List<AgeGroupDTO> applicantAgeGroupChart;          // 연령 분포
    private List<TopCompanyJobPostingsDTO> top5Companies;      // 상위 5개 기업
    private List<ApplicationTimeStatDTO> applicationTimeStats; // 시간대별 통계
}
