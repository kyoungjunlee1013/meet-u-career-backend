package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class JobPostingStats {
    private DashboardMetricDTO totalJobPostings;              // 전체 공고 수
    private DashboardMetricDTO activeJobPostings;             // 활성 공고 수
    private DashboardMetricDTO participatingCompanies;        // 참여 기업 수
    private DashboardMetricDTO totalViews;                    // 전체 조회수
    private List<MonthlyJobPostingCountDTO> jobPostingGrowthChart; // 월별 공고 수
    private List<JobCategoryCountDTO> jobCategoryChart;       // 카테고리 분포
    private List<JobCategoryCountDTO> jobCategoryPostings;    // 직무별 공고
    private JobPostingStatisticsDTO jobPostingStatistics;     // 공고 상태 통계
    private List<PopularKeywordJobPostingsDTO> keywordStatistics;           // 인기 키워드
    private List<TopCompanyJobPostingsDTO> topCompanies;      // 상위 기업 리스트
}
