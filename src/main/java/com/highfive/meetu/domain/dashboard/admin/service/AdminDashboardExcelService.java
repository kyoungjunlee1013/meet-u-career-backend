package com.highfive.meetu.domain.dashboard.admin.service;

import com.highfive.meetu.domain.dashboard.admin.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardExcelService {
    private final AdminDashboardService adminDashboardService;

    /**
     * 대시보드 보고서 엑셀 생성
     */
    public DashboardExcelResult generateDashboardExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // 스타일 생성
            CellStyle coverTitleStyle = createCoverTitleStyle(workbook);
            CellStyle generatedDateStyle = createGeneratedDateStyle(workbook);
            CellStyle sectionTitleStyle = createSectionTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // 데이터 조회
            var userStats = adminDashboardService.getUserStats();
            var jobPostingStats = adminDashboardService.getJobPostingStats();
            var applicationStats = adminDashboardService.getApplicationStats();

            // 0. 커버 페이지 생성
            XSSFSheet coverSheet = workbook.createSheet("대시보드 리포트");
            applyCoverPage(coverSheet, coverTitleStyle, generatedDateStyle);

            // 1. 회원 통계 시트
            XSSFSheet userSheet = workbook.createSheet("회원 통계");
            int userRowIdx = insertGeneratedDate(userSheet, generatedDateStyle);
            userRowIdx = insertSectionTitle(userSheet, userRowIdx, "사용자 통계", sectionTitleStyle);
            userRowIdx = insertUserStats(userSheet, userRowIdx, userStats, headerStyle, dataStyle);
            userRowIdx = insertSectionTitle(userSheet, userRowIdx, "사용자 유형 분포", sectionTitleStyle);
            userRowIdx = insertUserTypeStats(userSheet, userRowIdx, userStats.getUserTypeChart(), headerStyle, dataStyle);
            userRowIdx = insertSectionTitle(userSheet, userRowIdx, "월별 사용자 증가 추이 (그래프)", sectionTitleStyle);
            insertUserGrowthChart(workbook, userSheet, userRowIdx, userStats.getUserGrowthChart());

            // 2. 채용 통계 시트
            XSSFSheet jobSheet = workbook.createSheet("채용 통계");
            int jobRowIdx = insertGeneratedDate(jobSheet, generatedDateStyle);
            jobRowIdx = insertSectionTitle(jobSheet, jobRowIdx, "채용공고 통계", sectionTitleStyle);
            jobRowIdx = insertJobPostingStats(jobSheet, jobRowIdx, jobPostingStats, headerStyle, dataStyle);
            jobRowIdx = insertSectionTitle(jobSheet, jobRowIdx, "카테고리별 공고 수", sectionTitleStyle);
            jobRowIdx = insertJobCategoryStats(jobSheet, jobRowIdx, applicationStats.getJobCategoryChart(), headerStyle, dataStyle);
            jobRowIdx = insertSectionTitle(jobSheet, jobRowIdx, "지역별 채용공고 수", sectionTitleStyle);
            jobRowIdx = insertLocationStats(jobSheet, jobRowIdx, jobPostingStats.getLocationStatistics(), headerStyle, dataStyle);
            jobRowIdx = insertSectionTitle(jobSheet, jobRowIdx, "월별 채용공고 증가 추이 (그래프)", sectionTitleStyle);
            insertJobPostingGrowthChart(workbook, jobSheet, jobRowIdx, jobPostingStats.getJobPostingGrowthChart());

            // 3. 지원 통계 시트
            XSSFSheet applicationSheet = workbook.createSheet("지원 통계");
            int applicationRowIdx = insertGeneratedDate(applicationSheet, generatedDateStyle);
            applicationRowIdx = insertSectionTitle(applicationSheet, applicationRowIdx, "지원 통계", sectionTitleStyle);
            applicationRowIdx = insertApplicationStats(applicationSheet, applicationRowIdx, applicationStats, headerStyle, dataStyle);
            applicationRowIdx = insertSectionTitle(applicationSheet, applicationRowIdx, "지원자 연령대 분포", sectionTitleStyle);
            applicationRowIdx = insertApplicantAgeGroupStats(applicationSheet, applicationRowIdx, applicationStats.getApplicantAgeGroupChart(), headerStyle, dataStyle);
            applicationRowIdx = insertSectionTitle(applicationSheet, applicationRowIdx, "지원자 많은 TOP 5 기업", sectionTitleStyle);
            applicationRowIdx = insertTop5CompaniesStats(applicationSheet, applicationRowIdx, applicationStats.getTop5Companies(), headerStyle, dataStyle);
            applicationRowIdx = insertSectionTitle(applicationSheet, applicationRowIdx, "일별 지원자 수 추이 (그래프)", sectionTitleStyle);
            insertDailyApplicationChart(workbook, applicationSheet, applicationRowIdx, applicationStats.getApplicationTrends());

            // 시트 탭 색상 설정
            coverSheet.setTabColor(new XSSFColor(new Color(173, 216, 230), new DefaultIndexedColorMap())); // 연한 하늘색
            userSheet.setTabColor(new XSSFColor(new Color(189, 215, 238), new DefaultIndexedColorMap())); // 연한 파란색
            jobSheet.setTabColor(new XSSFColor(new Color(198, 239, 206), new DefaultIndexedColorMap()));  // 연한 초록색
            applicationSheet.setTabColor(new XSSFColor(new Color(255, 229, 153), new DefaultIndexedColorMap())); // 연한 노란색

            // 열 너비 조정
            for (XSSFSheet sheet : List.of(userSheet, jobSheet, applicationSheet)) {
                sheet.setColumnWidth(0, 6000);
                sheet.setColumnWidth(1, 5000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(3, 5000);
            }

            // 파일명 생성 (예: dashboard_report_2025_04.xlsx)
            String fileName = generateFileName();

            // 파일 작성
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return new DashboardExcelResult(fileName, bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("대시보드 보고서 생성 중 오류 발생", e);
        }
    }

    /**
     * 커버 페이지 타이틀 스타일 생성
     */
    private CellStyle createCoverTitleStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 24);
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 생성일(yyyy년 MM월 기준) 스타일 생성
     */
    private CellStyle createGeneratedDateStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setItalic(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 섹션 제목(예: "사용자 통계") 스타일 생성
     */
    private CellStyle createSectionTitleStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 테이블 헤더 스타일 생성
     */
    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * 테이블 데이터 셀 스타일 생성
     */
    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        return style;
    }

    /**
     * 커버 페이지 작성
     */
    private void applyCoverPage(XSSFSheet sheet, CellStyle coverTitleStyle, CellStyle generatedDateStyle) {
        // 생성일
        Row dateRow = sheet.createRow(2);
        Cell dateCell = dateRow.createCell(0);
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 기준");
        dateCell.setCellValue(now.format(formatter));
        dateCell.setCellStyle(generatedDateStyle);

        // 커버 타이틀
        Row titleRow = sheet.createRow(5);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("대시보드 통계 리포트");
        titleCell.setCellStyle(coverTitleStyle);

        // 타이틀 셀 병합
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 3));

        // 열 너비 조정
        for (int i = 0; i <= 3; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        // 시트 탭 색상 설정
        sheet.setTabColor(new XSSFColor(new Color(173, 216, 230), null));
    }

    /**
     * 파일명 자동 생성
     */
    private String generateFileName() {
        String baseName = "회원_채용_지원_통계_리포트";
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
        return baseName + "_" + datePart + ".xlsx";
    }

    /**
     * 생성일 삽입
     */
    private int insertGeneratedDate(XSSFSheet sheet, CellStyle dateStyle) {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 기준")));
        cell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        return 2;
    }

    /**
     * 섹션 타이틀 삽입
     */
    private int insertSectionTitle(XSSFSheet sheet, int rowIdx, String title, CellStyle titleStyle) {
        Row titleRow = sheet.createRow(rowIdx);
        Cell cell = titleRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 3));
        return rowIdx + 2;
    }

    /**
     * 공통 테이블 삽입
     */
    private int insertTable(XSSFSheet sheet, int rowIdx, String[] headers, Object[][] data, CellStyle headerStyle, CellStyle dataStyle) {
        Row headerRow = sheet.createRow(rowIdx++);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowIdx++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                if (rowData[i] instanceof Number) {
                    cell.setCellValue(((Number) rowData[i]).doubleValue());
                } else {
                    cell.setCellValue(rowData[i].toString());
                }
                cell.setCellStyle(dataStyle);
            }
        }
        rowIdx++; // 표 아래 공백 1줄 추가
        return rowIdx;
    }

    /**
     * 사용자 통계 삽입
     */
    private int insertUserStats(XSSFSheet sheet, int rowIdx, UserStats stats, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"구분", "현재 수", "전월 수", "성장률(%)"};
        Object[][] data = {
            {"개인회원 수", stats.getUserCount().getCurrent(), stats.getUserCount().getPrevious(), stats.getUserCount().getGrowthRate()},
            {"기업회원 수", stats.getCompanyCount().getCurrent(), stats.getCompanyCount().getPrevious(), stats.getCompanyCount().getGrowthRate()},
            {"채용공고 수", stats.getJobPostingCount().getCurrent(), stats.getJobPostingCount().getPrevious(), stats.getJobPostingCount().getGrowthRate()},
            {"커뮤니티 게시글 수", stats.getCommunityPostCount().getCurrent(), stats.getCommunityPostCount().getPrevious(), stats.getCommunityPostCount().getGrowthRate()}
        };
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 채용공고 통계 삽입
     */
    private int insertJobPostingStats(XSSFSheet sheet, int rowIdx, JobPostingStats stats, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"구분", "현재 수", "전월 수", "성장률(%)"};
        Object[][] data = {
            {"총 채용공고 수", stats.getTotalJobPostings().getCurrent(), stats.getTotalJobPostings().getPrevious(), stats.getTotalJobPostings().getGrowthRate()},
            {"진행중 채용공고 수", stats.getActiveJobPostings().getCurrent(), stats.getActiveJobPostings().getPrevious(), stats.getActiveJobPostings().getGrowthRate()},
            {"참여 기업 수", stats.getParticipatingCompanies().getCurrent(), stats.getParticipatingCompanies().getPrevious(), stats.getParticipatingCompanies().getGrowthRate()},
            {"총 조회수", stats.getTotalViews().getCurrent(), stats.getTotalViews().getPrevious(), stats.getTotalViews().getGrowthRate()}
        };
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 지원 통계 삽입
     */
    private int insertApplicationStats(XSSFSheet sheet, int rowIdx, ApplicationStats stats, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"구분", "현재 수", "전월 수", "성장률(%)"};
        Object[][] data = {
            {"총 지원 수", stats.getTotalApplications().getCurrent(), stats.getTotalApplications().getPrevious(), stats.getTotalApplications().getGrowthRate()},
            {"서류 합격 수", stats.getAcceptedApplications().getCurrent(), stats.getAcceptedApplications().getPrevious(), stats.getAcceptedApplications().getGrowthRate()},
            {"서류 불합격 수", stats.getRejectedApplications().getCurrent(), stats.getRejectedApplications().getPrevious(), stats.getRejectedApplications().getGrowthRate()}
        };
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 사용자 유형 분포 삽입
     */
    private int insertUserTypeStats(XSSFSheet sheet, int rowIdx, List<UserTypeCountDTO> dataList, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"유형", "회원 수"};
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = userTypeName(dataList.get(i).getAccountType());
            data[i][1] = dataList.get(i).getCount();
        }
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 지원자 연령대 분포 삽입
     */
    private int insertApplicantAgeGroupStats(XSSFSheet sheet, int rowIdx, List<AgeGroupDTO> dataList, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"연령대", "지원자 수"};
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).getAgeGroup();
            data[i][1] = dataList.get(i).getCount();
        }
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 카테고리별 공고 수 삽입
     */
    private int insertJobCategoryStats(XSSFSheet sheet, int rowIdx, List<JobCategoryCountDTO> dataList, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"카테고리", "공고 수"};
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).getCategoryName();
            data[i][1] = dataList.get(i).getJobPostingCount();
        }
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 지역별 채용공고 수 삽입
     */
    private int insertLocationStats(XSSFSheet sheet, int rowIdx, List<LocationJobPostingStatsDTO> dataList, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"지역", "공고 수"};
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).getLocationName();
            data[i][1] = dataList.get(i).getJobPostingCount();
        }
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 지원자 많은 TOP 5 기업 삽입
     */
    private int insertTop5CompaniesStats(XSSFSheet sheet, int rowIdx, List<TopCompanyJobPostingsDTO> dataList, CellStyle headerStyle, CellStyle dataStyle) {
        String[] headers = {"기업명", "지원자 수"};
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).getCompanyName();
            data[i][1] = dataList.get(i).getJobPostingCount();
        }
        return insertTable(sheet, rowIdx, headers, data, headerStyle, dataStyle);
    }

    /**
     * 사용자 유형 이름 변환
     */
    private String userTypeName(Integer userType) {
        return switch (userType) {
            case 0 -> "개인회원";
            case 1 -> "기업회원";
            case 2 -> "관리자";
            default -> "기타";
        };
    }

    /**
     * 공통 LineChart 삽입
     */
    private void insertLineChart(XSSFWorkbook workbook, XSSFSheet sheet, int rowIdx, List<String> labels, List<Long> values, String xTitle, String yTitle, String chartTitle) {
        int startRow = rowIdx + 1;
        int colLabel = 0;
        int colValue = 1;

        for (int i = 0; i < labels.size(); i++) {
            Row row = sheet.createRow(startRow + i);
            row.createCell(colLabel).setCellValue(labels.get(i));
            row.createCell(colValue).setCellValue(values.get(i));
        }

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, startRow, 10, startRow + 15);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(chartTitle);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle(xTitle);
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(yTitle);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(startRow, startRow + labels.size() - 1, colLabel, colLabel));
        XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(startRow, startRow + labels.size() - 1, colValue, colValue));

        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(xData, yData);
        series.setTitle(chartTitle, null);
        series.setSmooth(false);
        series.setMarkerStyle(MarkerStyle.CIRCLE);

        chart.plot(data);
    }

    /**
     * 월별 사용자 증가 추이 그래프 삽입
     */
    private void insertUserGrowthChart(XSSFWorkbook workbook, XSSFSheet sheet, int rowIdx, List<MonthlyUserCountDTO> data) {
        insertLineChart(workbook, sheet, rowIdx,
            data.stream().map(MonthlyUserCountDTO::getMonth).toList(),
            data.stream().map(MonthlyUserCountDTO::getUserCount).toList(),
            "월", "사용자 수", "월별 사용자 증가 추이");
    }

    /**
     * 월별 채용공고 증가 추이 그래프 삽입
     */
    private void insertJobPostingGrowthChart(XSSFWorkbook workbook, XSSFSheet sheet, int rowIdx, List<MonthlyJobPostingCountDTO> data) {
        insertLineChart(workbook, sheet, rowIdx,
            data.stream().map(MonthlyJobPostingCountDTO::getMonth).toList(),
            data.stream().map(MonthlyJobPostingCountDTO::getJobPostingCount).toList(),
            "월", "공고 수", "월별 채용공고 증가 추이");
    }

    /**
     * 일별 지원자 수 추이 그래프 삽입
     */
    private void insertDailyApplicationChart(XSSFWorkbook workbook, XSSFSheet sheet, int rowIdx, List<DailyApplicationStatsDTO> data) {
        insertLineChart(workbook, sheet, rowIdx,
            data.stream().map(d -> d.getDate().toString()).toList(),
            data.stream().map(DailyApplicationStatsDTO::getTotalApplications).toList(),
            "날짜", "지원자 수", "일별 지원자 수 추이");
    }
}
