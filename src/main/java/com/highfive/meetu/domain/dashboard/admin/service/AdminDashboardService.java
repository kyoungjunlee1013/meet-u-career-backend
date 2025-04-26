package com.highfive.meetu.domain.dashboard.admin.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.dashboard.admin.dto.*;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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

    /**
     * 전환율 계산 (0 나누기 방지)
     */
    private double calculateConversionRate(long numerator, long denominator) {
        return denominator == 0 ? 0 : ((double) numerator / denominator) * 100;
    }

    // ✅ 엑셀 다운로드용 메서드
    public byte[] generateDashboardExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Dashboard Report");

            int rowIdx = 0;

            var userStats = getUserStats();
            rowIdx = writeUserStats(sheet, rowIdx, userStats);
            rowIdx++;

            var jobPostingStats = getJobPostingStats();
            rowIdx = writeJobPostingStats(sheet, rowIdx, jobPostingStats);
            rowIdx++;

            var applicationStats = getApplicationStats();
            rowIdx = writeApplicationStats(sheet, rowIdx, applicationStats);
            rowIdx++;

            insertUserGrowthChart(workbook, sheet, rowIdx, userStats.getUserGrowthChart());

            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("대시보드 보고서 생성 중 오류 발생", e);
        }
    }

    private int writeUserStats(XSSFSheet sheet, int rowIdx, UserStats userStats) {
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("사용자 통계");
        Row column = sheet.createRow(rowIdx++);
        column.createCell(0).setCellValue("구분");
        column.createCell(1).setCellValue("현재 수");
        column.createCell(2).setCellValue("전월 수");
        column.createCell(3).setCellValue("성장률");

        rowIdx = createMetricRow(sheet, rowIdx, "개인회원 수", userStats.getUserCount());
        rowIdx = createMetricRow(sheet, rowIdx, "기업회원 수", userStats.getCompanyCount());
        rowIdx = createMetricRow(sheet, rowIdx, "채용공고 수", userStats.getJobPostingCount());
        rowIdx = createMetricRow(sheet, rowIdx, "커뮤니티 게시글 수", userStats.getCommunityPostCount());
        return rowIdx;
    }

    private int writeJobPostingStats(XSSFSheet sheet, int rowIdx, JobPostingStats jobPostingStats) {
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("채용공고 통계");

        rowIdx = createMetricRow(sheet, rowIdx, "총 채용공고 수", jobPostingStats.getTotalJobPostings());
        rowIdx = createMetricRow(sheet, rowIdx, "진행중 채용공고 수", jobPostingStats.getActiveJobPostings());
        rowIdx = createMetricRow(sheet, rowIdx, "참여 기업 수", jobPostingStats.getParticipatingCompanies());
        rowIdx = createMetricRow(sheet, rowIdx, "총 조회수", jobPostingStats.getTotalViews());
        return rowIdx;
    }

    private int writeApplicationStats(XSSFSheet sheet, int rowIdx, ApplicationStats applicationStats) {
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("지원 통계");

        rowIdx = createMetricRow(sheet, rowIdx, "총 지원 수", applicationStats.getTotalApplications());
        rowIdx = createMetricRow(sheet, rowIdx, "서류 합격 수", applicationStats.getAcceptedApplications());
        rowIdx = createMetricRow(sheet, rowIdx, "서류 불합격 수", applicationStats.getRejectedApplications());
        return rowIdx;
    }

    private int createMetricRow(XSSFSheet sheet, int rowIdx, String label, DashboardMetricDTO metric) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(metric.getCurrent());
        row.createCell(2).setCellValue(metric.getPrevious());
        row.createCell(3).setCellValue(metric.getGrowthRate() + "%");
        return rowIdx;
    }

    // 월별 사용자수 그래프 삽입
    private void insertUserGrowthChart(XSSFWorkbook workbook, XSSFSheet sheet, int rowIdx, List<MonthlyUserCountDTO> growthData) {
        int chartStartRow = rowIdx + 2;
        int chartStartCol = 0;

        for (int i = 0; i < growthData.size(); i++) {
            Row row = sheet.createRow(chartStartRow + i);
            row.createCell(chartStartCol).setCellValue(growthData.get(i).getMonth());
            row.createCell(chartStartCol + 1).setCellValue(growthData.get(i).getUserCount());
        }

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, chartStartRow, 10, chartStartRow + 15);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("월별 사용자 증가 추이");
        chart.setTitleOverlay(false);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("월");

        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("사용자 수");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        XDDFDataSource<String> months = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(chartStartRow, chartStartRow + growthData.size() - 1, chartStartCol, chartStartCol));
        XDDFNumericalDataSource<Double> users = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(chartStartRow, chartStartRow + growthData.size() - 1, chartStartCol + 1, chartStartCol + 1));

        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(months, users);
        series.setTitle("사용자 수", null);
        series.setSmooth(false);
        series.setMarkerStyle(MarkerStyle.CIRCLE);

        chart.plot(data);
    }
}
