package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class UserStats {
    private DashboardMetricDTO userCount;              // 전체 사용자 수
    private DashboardMetricDTO companyCount;           // 기업 수
    private DashboardMetricDTO jobPostingCount;        // 전체 채용공고 수
    private DashboardMetricDTO communityPostCount;     // 커뮤니티 글 수
    private List<MonthlyUserCountDTO> userGrowthChart; // 사용자 증가 추이
    private List<UserTypeCountDTO> userTypeChart;      // 사용자 유형 분포
}