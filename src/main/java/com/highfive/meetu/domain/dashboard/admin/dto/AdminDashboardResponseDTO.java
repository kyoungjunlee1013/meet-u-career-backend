package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminDashboardResponseDTO {
    private DashboardMetricDTO userCount; // 사용자 수
    private DashboardMetricDTO companyCount; // 기업 수
    private DashboardMetricDTO jobPostingCount; // 채용공고 수
    private DashboardMetricDTO communityPostCount; // 커뮤니티 게시글

    private List<MonthlyUserCountDTO> userGrowthChart;   // 사용자 증가 추이
    private List<UserTypeCountDTO> userTypeChart;        // 사용자 유형 분포
    private List<MonthlyJobPostingCountDTO> jobPostingGrowthChart;  // 채용공고 증가 추이
    private List<JobCategoryCountDTO> jobCategoryChart;  // 채용공고 카테고리 분포
    private List<PopularJobPostingDTO> popularJobPostings;  // 인기 채용공고

    private DashboardMetricDTO totalJobPostings;   // 총 채용공고
    private DashboardMetricDTO activeJobPostings;  // 활성 채용공고
    private DashboardMetricDTO participatingCompanies;  // 참여 기업
    private DashboardMetricDTO totalViews;  // 조회수

    private List<JobCategoryCountDTO> jobCategoryPostings;  // 직무별 채용공고
    private List<JobPostingStatusDTO> jobPostingStatusChart; // 채용공고 상태별 통계

    private List<TopCompanyJobPostingsDTO> topCompanies;   // 채용공고 상위 TOP 5
    private List<PopularJobKeywordDTO> popularJobKeywords;  // 인기 채용 키워드

    private DashboardMetricDTO totalApplications;  // 총 지원 건수
    private DashboardMetricDTO inProgressApplications;  // 진행 중인 지원 건수
    private DashboardMetricDTO acceptedApplications;  // 합격한 지원 건수
    private DashboardMetricDTO rejectedApplications;  // 불합격한 지원 건수

    private List<DashboardMetricDTO> applicationTrends; // 지원 추이
    private List<ConversionRateDTO> conversionRates; // 지원 전환율
    private List<AgeGroupDTO> applicantAgeGroupChart; // 지원자 연령 분포

    private List<TopCompanyJobPostingsDTO> top5Companies; // 지원자 많은 기업 TOP 5
    private List<ApplyTimeDTO> applyTimeChart;  // 지원 시간 분석

    public AdminDashboardResponseDTO(
        DashboardMetricDTO userCount,
        DashboardMetricDTO companyCount,
        DashboardMetricDTO jobPostingCount,
        DashboardMetricDTO communityPostCount,
        List<MonthlyUserCountDTO> userGrowthChart,
        List<UserTypeCountDTO> userTypeChart,
        List<MonthlyJobPostingCountDTO> jobPostingGrowthChart,
        List<JobCategoryCountDTO> jobCategoryChart,
        List<PopularJobPostingDTO> popularJobPostings,
        DashboardMetricDTO totalJobPostings,
        DashboardMetricDTO activeJobPostings,
        DashboardMetricDTO participatingCompanies,
        DashboardMetricDTO totalViews,
        List<JobCategoryCountDTO> jobCategoryPostings,
        List<JobPostingStatusDTO> jobPostingStatusChart,
        List<TopCompanyJobPostingsDTO> topCompanies,
        List<PopularJobKeywordDTO> popularJobKeywords,
        DashboardMetricDTO totalApplications,
        DashboardMetricDTO inProgressApplications,
        DashboardMetricDTO acceptedApplications,
        DashboardMetricDTO rejectedApplications,
        List<ConversionRateDTO> conversionRates,
        List<AgeGroupDTO> applicantAgeGroupChart,
        List<ApplyTimeDTO> applyTimeChart
    ) {
        this.userCount = userCount;
        this.companyCount = companyCount;
        this.jobPostingCount = jobPostingCount;
        this.communityPostCount = communityPostCount;
        this.userGrowthChart = userGrowthChart;
        this.userTypeChart = userTypeChart;
        this.jobPostingGrowthChart = jobPostingGrowthChart;
        this.jobCategoryChart = jobCategoryChart;
        this.popularJobPostings = popularJobPostings;
        this.totalJobPostings = totalJobPostings;
        this.activeJobPostings = activeJobPostings;
        this.participatingCompanies = participatingCompanies;
        this.totalViews = totalViews;
        this.jobCategoryPostings = jobCategoryPostings;
        this.jobPostingStatusChart = jobPostingStatusChart;
        this.topCompanies = topCompanies;
        this.popularJobKeywords = popularJobKeywords;
        this.totalApplications = totalApplications;
        this.inProgressApplications = inProgressApplications;
        this.acceptedApplications = acceptedApplications;
        this.rejectedApplications = rejectedApplications;
        this.conversionRates = conversionRates;
        this.applicantAgeGroupChart = applicantAgeGroupChart;
        this.applyTimeChart = applyTimeChart;
    }
}