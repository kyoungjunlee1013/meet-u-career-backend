package com.highfive.meetu.domain.dashboard.admin.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.dashboard.admin.dto.*;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final ApplicationRepository applicationRepository;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CommunityPostRepository communityPostRepository;

    /**
     * 사용자 관련 통계 정보 조회
     */
    public UserStats getUserStats() {
        // 날짜 계산
        LocalDateTime currentStart = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime previousStart = currentStart.withDayOfMonth(currentStart.toLocalDate().lengthOfMonth());
        LocalDateTime previousEnd = previousStart.withHour(23).withMinute(59).withSecond(59);

        // 사용자, 기업, 공고, 커뮤니티 수 계산
        DashboardMetricDTO userCount = build(accountRepository.countUsersCurrent(currentStart), accountRepository.countUsersPrevious(previousStart, previousEnd));
        DashboardMetricDTO companyCount = build(companyRepository.countCompaniesCurrent(currentStart), companyRepository.countCompaniesPrevious(previousStart, previousEnd));
        DashboardMetricDTO jobPostingCount = build(jobPostingRepository.countJobPostingsCurrent(currentStart), jobPostingRepository.countJobPostingsPrevious(previousStart, previousEnd));
        DashboardMetricDTO communityPostCount = build(communityPostRepository.countPostsCurrent(currentStart), communityPostRepository.countPostsPrevious(previousStart, previousEnd));

        // 사용자 증가 추이 (월별)
        List<MonthlyUserCountDTO> userGrowthChart = accountRepository.countUsersByMonth(2025).stream()
            .map(row -> new MonthlyUserCountDTO(Integer.parseInt(((String) row[0]).substring(5)) + "월", (Long) row[1]))
            .toList();

        // 사용자 유형 분포 (개인/기업 등)
        List<UserTypeCountDTO> userTypeChart = accountRepository.countUsersByType().stream()
            .map(row -> new UserTypeCountDTO((Integer) row[0], (Long) row[1]))
            .toList();

        return new UserStats(userCount, companyCount, jobPostingCount, communityPostCount, userGrowthChart, userTypeChart);
    }

    /**
     * 채용공고 관련 통계 정보 조회
     */
    public JobPostingStats getJobPostingStats() {
        // 메트릭 지표
        DashboardMetricDTO totalJobPostings = build(jobPostingRepository.countTotalJobPostings(), 0);
        DashboardMetricDTO activeJobPostings = build(jobPostingRepository.countJobPostingsByStatus(2), 0);
        DashboardMetricDTO participatingCompanies = build(companyRepository.countParticipatingCompanies(), 0);
        DashboardMetricDTO totalViews = build(jobPostingRepository.countTotalViews(), 0);

        // 월별 공고 증가 추이
        List<MonthlyJobPostingCountDTO> jobPostingGrowthChart = jobPostingRepository.countJobPostingsByMonth(2025).stream()
            .map(row -> new MonthlyJobPostingCountDTO(Integer.parseInt(((String) row[0]).substring(5)) + "월", (Long) row[1]))
            .toList();

        // 상위 기업별 공고 수
        List<TopCompanyJobPostingsDTO> topCompanies = jobPostingRepository.countTopCompaniesJobPostings(2).stream()
            .map(row -> new TopCompanyJobPostingsDTO((String) row[0], (Long) row[1]))
            .toList();

        /**
         * 인기 키워드 (공고 키워드 기준)
         */
        List<PopularKeywordJobPostingsDTO> popularKeywordJobPostings = jobPostingRepository.findTopKeywordsRaw().stream()
            .map(row -> new PopularKeywordJobPostingsDTO((String) row[0], ((Number) row[1]).longValue()))
            .toList();

        // 채용공고 상태 통계
        long active = jobPostingRepository.countActiveJobPostings();
        long expired = jobPostingRepository.countExpiredJobPostings();
        long draft = jobPostingRepository.countDraftJobPostings();
        double avgView = Optional.ofNullable(jobPostingRepository.findAverageViewCount()).orElse(0.0);
        double avgApply = Optional.ofNullable(jobPostingRepository.findAverageApplyCount()).orElse(0.0);
        double avgDays = Optional.ofNullable(jobPostingRepository.findAveragePostingDays()).orElse(0.0);
        JobPostingStatisticsDTO jobPostingStatistics = new JobPostingStatisticsDTO(active, expired, draft, avgView, avgApply, avgDays);

        // 지역별 채용공고 수
        List<LocationJobPostingStatsDTO> locationStatistics =  jobPostingRepository.countJobPostingsByLocation().stream()
            .map(row -> new LocationJobPostingStatsDTO((String) row[0], (Long) row[1]))  // 지역명, 공고 수
            .toList();

        return new JobPostingStats(
            totalJobPostings,
            activeJobPostings,
            participatingCompanies,
            totalViews,
            jobPostingGrowthChart,
            jobPostingStatistics,
            popularKeywordJobPostings,
            topCompanies,
            locationStatistics
        );
    }

    /**
     * 지원 관련 통계 정보 조회
     */
    public ApplicationStats getApplicationStats() {
        // 전월 기준 날짜
        LocalDateTime previousStart = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();

        // 지원 건수 관련 메트릭
        DashboardMetricDTO totalApplications = build(applicationRepository.countTotalApplications(), 0);
        DashboardMetricDTO acceptedApplications = build(applicationRepository.countAcceptedApplications(), 0);
        DashboardMetricDTO rejectedApplications = build(applicationRepository.countRejectedApplications(), 0);

        // 최근 2주간 일별 지원 추이
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime start = today.minusWeeks(2);
        LocalDateTime end = today.plusDays(1).minusNanos(1); // 오늘의 23:59:59
        List<DailyApplicationStatsDTO> applicationTrends = applicationRepository.findDailyApplicationStats(start, end)
            .stream()
            .map(row -> new DailyApplicationStatsDTO(
                ((java.sql.Date) row[0]).toLocalDate(),
                ((Number) row[1]).longValue(),  // totalApplications
                ((Number) row[2]).longValue(),  // acceptedApplications
                ((Number) row[3]).longValue()   // rejectedApplications
            ))
            .toList();

        // 연령대 분포
        List<AgeGroupDTO> applicantAgeGroupChart = applicationRepository.countApplicantsByAgeGroup().stream()
            .map(row -> new AgeGroupDTO((String) row[0], (Long) row[1]))
            .toList();

        // 카테고리 분포
        List<JobCategoryCountDTO> jobCategoryChart = jobPostingRepository.countJobPostingsByCategory().stream()
            .map(row -> new JobCategoryCountDTO((String) row[0], (Long) row[1]))
            .toList();

        // 직무별 공고 수
        List<JobCategoryCountDTO> jobCategoryPostings = jobPostingRepository.countJobPostingsByJobCategory().stream()
            .map(row -> new JobCategoryCountDTO((String) row[0], (Long) row[1]))
            .toList();

        // 지원자 많은 상위 5개 기업
        Pageable topFive = PageRequest.of(0, 5);
        List<TopCompanyJobPostingsDTO> top5Companies = applicationRepository.countTopCompaniesByApplicants(topFive).stream()
            .map(row -> new TopCompanyJobPostingsDTO((String) row[0], (Long) row[1]))
            .toList();

        // 시간대별 지원 통계
        List<ApplicationTimeStatDTO> applicationTimeStats = applicationRepository.findApplicationTimeStatsHourly().stream()
            .map(row -> new ApplicationTimeStatDTO(
                (String) row[0],
                ((Number) row[1]).intValue()
            )).toList();

        return new ApplicationStats(
            totalApplications,
            acceptedApplications,
            rejectedApplications,
            applicationTrends,
            applicantAgeGroupChart,
            top5Companies,
            jobCategoryChart,
            jobCategoryPostings,
            applicationTimeStats
        );
    }

    /**
     * 성장률 포함된 지표 DTO 생성
     */
    private DashboardMetricDTO build(long current, long previous) {
        double rate = (previous == 0) ? 100.0 : ((double)(current - previous) / previous) * 100;
        return new DashboardMetricDTO(current, previous, Math.round(rate * 10.0) / 10.0);
    }
}
