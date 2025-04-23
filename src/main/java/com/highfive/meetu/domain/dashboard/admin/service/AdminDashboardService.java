package com.highfive.meetu.domain.dashboard.admin.service;

import com.highfive.meetu.domain.dashboard.admin.dto.*;
import com.highfive.meetu.domain.dashboard.admin.repository.AdminDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final AdminDashboardRepository repository;

    public AdminDashboardResponseDTO getMetrics() {
        // 기준 날짜 설정
        String currentStart = "2025-04-01";
        String previousStart = "2025-03-01";
        String previousEnd = "2025-03-31";

        // 기존 4개 카드 통계 항목 처리
        DashboardMetricDTO userCount = build(
            repository.countUsersCurrent(currentStart),
            repository.countUsersPrevious(previousStart, previousEnd)
        );

        DashboardMetricDTO companyCount = build(
            repository.countCompaniesCurrent(currentStart),
            repository.countCompaniesPrevious(previousStart, previousEnd)
        );

        DashboardMetricDTO jobPostingCount = build(
            repository.countJobPostingsCurrent(currentStart),
            repository.countJobPostingsPrevious(previousStart, previousEnd)
        );

        DashboardMetricDTO communityPostCount = build(
            repository.countPostsCurrent(currentStart),
            repository.countPostsPrevious(previousStart, previousEnd)
        );

        // 사용자 증가 추이 조회 (월별 누적 사용자 수)
        List<MonthlyUserCountDTO> userGrowthChart = repository.countUsersByMonth(2025).stream()
            .map(row -> {
                String ym = (String) row[0]; // 예: "2025-01"
                Long count = (Long) row[1];
                String month = Integer.parseInt(ym.substring(5)) + "월"; // "01" → "1월"
                return new MonthlyUserCountDTO(month, count);
            })
            .toList();

        // 사용자 유형 분포
        List<UserTypeCountDTO> userTypeChart = repository.countUsersByType().stream()
            .map((Object[] row) -> {
                String type = (String) row[0]; // "personal", "business", "admin"
                Long count = (Long) row[1];
                return new UserTypeCountDTO(type, count);
            })
            .toList();

        // 채용공고 증가 추이 조회 (월별 채용공고 수)
        List<MonthlyJobPostingCountDTO> jobPostingGrowthChart = repository.countJobPostingsByMonth(2025).stream()
            .map(row -> {
                String ym = (String) row[0]; // 예: "2025-01"
                Long count = (Long) row[1];
                String month = Integer.parseInt(ym.substring(5)) + "월"; // "01" → "1월"
                return new MonthlyJobPostingCountDTO(month, count);
            })
            .toList();

        // 채용공고 카테고리 분포 조회
        List<JobCategoryCountDTO> jobCategoryChart = repository.countJobPostingsByCategory().stream()
            .map(row -> {
                String categoryName = (String) row[0]; // 직무명 (예: "개발", "디자인" 등)
                Long count = (Long) row[1];
                return new JobCategoryCountDTO(categoryName, count);
            })
            .toList();

        // 인기 채용공고 조회
        List<PopularJobPostingDTO> popularJobPostings = repository.countPopularJobPostings(2).stream()  // 2는 ACTIVE 상태
            .map(row -> {
                String jobTitle = (String) row[0];  // 공고 제목
                String companyName = (String) row[1];  // 회사명
                Long applicantCount = (Long) row[2];  // 지원자 수
                return new PopularJobPostingDTO(jobTitle, companyName, applicantCount);
            })
            .toList();

        // 직무별 채용공고 조회
        List<JobCategoryCountDTO> jobCategoryPostings = repository.countJobPostingsByJobCategory().stream()
            .map(row -> {
                String jobCategoryName = (String) row[0];  // 직무명 (예: "개발", "디자인" 등)
                Long jobCategoryPostingCount = (Long) row[1];  // 해당 직무의 채용공고 수
                return new JobCategoryCountDTO(jobCategoryName, jobCategoryPostingCount);
            })
            .toList();

        DashboardMetricDTO totalJobPostings = build(
            repository.countTotalJobPostings(),
            0 // 전년 대비 성장률 계산 불가
        );

        DashboardMetricDTO activeJobPostings = build(
            repository.countJobPostingsByStatus(2),  // 2는 ACTIVE 상태
            0 // 전년 대비 성장률 계산 불가
        );

        DashboardMetricDTO participatingCompanies = build(
            repository.countParticipatingCompanies(),
            0 // 전년 대비 성장률 계산 불가
        );

        DashboardMetricDTO totalViews = build(
            repository.countTotalViews(),
            0 // 전년 대비 성장률 계산 불가
        );

        // 채용공고 상태별 통계 처리
        List<JobPostingStatusDTO> jobPostingStatusChart = repository.countJobPostingsByStatus().stream()
            .map(row -> {
                String status = (String) row[0];  // 채용공고 상태 (활성, 마감, 임시저장)
                Long count = (Long) row[1];
                double percentage = (double) count / repository.countTotalJobPostings() * 100;
                return new JobPostingStatusDTO(status, count, Math.round(percentage * 10.0) / 10.0);
            })
            .toList();

        // 채용공고 상위 TOP 5 조회
        List<TopCompanyJobPostingsDTO> topCompanies = repository.countTopCompaniesJobPostings(2).stream()  // 2는 ACTIVE 상태
            .map(row -> {
                String companyName = (String) row[0];  // 회사명
                Long topCompanyJobPostingCount = (Long) row[1];  // 채용공고 수
                return new TopCompanyJobPostingsDTO(companyName, topCompanyJobPostingCount);
            })
            .toList();

        // 인기 채용 키워드 조회
        List<PopularJobKeywordDTO> popularJobKeywords = repository.countPopularJobKeywords(2).stream()  // 2는 ACTIVE 상태
            .map(row -> {
                String keyword = (String) row[0];  // 키워드
                Long count = (Long) row[1];  // 채용공고 수
                double growthRate = (double) row[2];  // 증가율
                return new PopularJobKeywordDTO(keyword, count, growthRate);
            })
            .toList();

        DashboardMetricDTO totalApplications = build(
            repository.countTotalApplications(),
            0 // previous 값은 0으로 설정
        );

        DashboardMetricDTO inProgressApplications = build(
            repository.countInProgressApplications(),
            0 // previous 값은 0으로 설정
        );

        DashboardMetricDTO acceptedApplications = build(
            repository.countAcceptedApplications(),
            0 // previous 값은 0으로 설정
        );

        DashboardMetricDTO rejectedApplications = build(
            repository.countRejectedApplications(),
            0 // previous 값은 0으로 설정
        );

        // 지원 추이 조회 (총 지원 건수, 합격, 불합격)
        List<DashboardMetricDTO> applicationTrends = new ArrayList<>();
        // 현재와 이전 값을 가져와서 인수로 전달
        applicationTrends.add(build(
            repository.countTotalApplications(),
            repository.countTotalApplicationsPrevious(previousStart) // 이전 값 가져오기
        ));

        applicationTrends.add(build(
            repository.countInProgressApplications(),
            repository.countInProgressApplicationsPrevious(previousStart) // 이전 값 가져오기
        ));

        applicationTrends.add(build(
            repository.countAcceptedApplications(),
            repository.countAcceptedApplicationsPrevious(previousStart) // 이전 값 가져오기
        ));

        applicationTrends.add(build(
            repository.countRejectedApplications(),
            repository.countRejectedApplicationsPrevious(previousStart) // 이전 값 가져오기
        ));

        // 지원 전환율
        double applicationToProgress = calculateConversionRate(repository.countTotalApplications(), repository.countInProgressApplications());
        double progressToAccepted = calculateConversionRate(repository.countInProgressApplications(), repository.countAcceptedApplications());
        double acceptedToFinal = calculateConversionRate(repository.countAcceptedApplications(), repository.countRejectedApplications());
        double applicationToFinal = calculateConversionRate(repository.countTotalApplications(), repository.countRejectedApplications());
        List<ConversionRateDTO> conversionRates = new ArrayList<>();
        conversionRates.add(new ConversionRateDTO("공고 조회 → 지원", applicationToProgress));
        conversionRates.add(new ConversionRateDTO("지원 → 서류 합격", progressToAccepted));
        conversionRates.add(new ConversionRateDTO("서류 합격 → 면접 합격", acceptedToFinal));
        conversionRates.add(new ConversionRateDTO("면접 합격 → 최종 합격", applicationToFinal));

        // 지원자 연령 분포 조회
        List<AgeGroupDTO> applicantAgeGroupChart = repository.countApplicantsByAgeGroup().stream()
            .map(row -> {
                String ageGroup = (String) row[0]; // 연령대 (예: "20대", "30대" 등)
                Long count = (Long) row[1];  // 해당 연령대의 지원자 수
                return new AgeGroupDTO(ageGroup, count);
            })
            .toList();

        // 지원자 많은 기업 TOP 5 조회
        List<TopCompanyJobPostingsDTO> top5Companies = repository.countTopCompaniesByApplicants().stream()
            .map(row -> {
                String companyName = (String) row[0];  // 회사명
                Long applicantCount = (Long) row[1];  // 지원자 수
                return new TopCompanyJobPostingsDTO(companyName, applicantCount);
            })
            .toList();

        // 지원 시간 통계 처리
        List<ApplyTimeDTO> applyTimeChart = repository.countApplyTime().stream()
            .map(row -> {
                String timeRange = (String) row[0]; // 시간대 (예: "오전", "1-3시", "4-7시" 등)
                double completionTime = (double) row[1]; // 평균 지원 완료 시간
                double cancellationTime = (double) row[2]; // 평균 지원 취소 시간
                double modificationTime = (double) row[3]; // 평균 지원 수정 시간
                return new ApplyTimeDTO(timeRange, completionTime, cancellationTime, modificationTime);
            })
            .toList();

        return new AdminDashboardResponseDTO(
            userCount,
            companyCount,
            jobPostingCount,
            communityPostCount,
            userGrowthChart,
            userTypeChart,
            jobPostingGrowthChart,
            jobCategoryChart,
            popularJobPostings,
            totalJobPostings,
            activeJobPostings,
            participatingCompanies,
            totalViews,
            jobCategoryPostings,
            jobPostingStatusChart,
            topCompanies,
            popularJobKeywords,
            totalApplications,
            inProgressApplications,
            acceptedApplications,
            rejectedApplications,
            applicationTrends,
            conversionRates,
            applicantAgeGroupChart,
            top5Companies,
            applyTimeChart
        );
    }

    private DashboardMetricDTO build(long current, long previous) {
        double rate = (previous == 0) ? 100.0 : ((double)(current - previous) / previous) * 100;
        return new DashboardMetricDTO(current, previous, Math.round(rate * 10.0) / 10.0);
    }

    private double calculateConversionRate(long numerator, long denominator) {
        return denominator == 0 ? 0 : ((double) numerator / denominator) * 100;
    }
}
