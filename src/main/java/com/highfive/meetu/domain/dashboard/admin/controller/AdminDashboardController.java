package com.highfive.meetu.domain.dashboard.admin.controller;

import com.highfive.meetu.domain.dashboard.admin.dto.*;
import com.highfive.meetu.domain.dashboard.admin.service.AdminDashboardExcelService;
import com.highfive.meetu.domain.dashboard.admin.service.AdminDashboardService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;
    private final AdminDashboardExcelService adminDashboardExcelService;

    /**
     * 관리자 대시보드 - 사용자 관련 통계 조회
     *
     * 응답 데이터: UserStats (사용자 수, 기업 수, 공고 수, 커뮤니티 게시글 수, 월별 사용자 증가 추이, 사용자 유형 분포 등)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userstats")
    public ResultData<UserStats> getUserStats() {
        return ResultData.success(1, adminDashboardService.getUserStats());
    }

    /**
     * 관리자 대시보드 - 채용공고 관련 통계 조회
     *
     * 응답 데이터: JobPostingStats
     * - 총 공고 수, 활성 공고 수, 참여 기업 수, 총 조회수
     * - 월별 공고 증가 추이, 카테고리별 분포, 직무별 공고 수
     * - 인기 키워드, 상위 기업 리스트, 공고 상태별 통계
     */
    @GetMapping("/jobpostingstats")
    public ResultData<JobPostingStats> getJobPostingStats() {
        return ResultData.success(1, adminDashboardService.getJobPostingStats());
    }

    /**
     * 관리자 대시보드 - 지원 관련 통계 조회
     *
     * 응답 데이터: ApplicationStats
     * - 총 지원 수, 진행 중/합격/불합격 수
     * - 지원 추이(월별 비교), 전환율, 연령 분포
     * - 지원자 많은 상위 기업, 시간대별 지원 분포
     */
    @GetMapping("/applicationstats")
    public ResultData<ApplicationStats> getApplicationStats() {
        return ResultData.success(1, adminDashboardService.getApplicationStats());
    }

    /**
     * 대시보드 보고서 엑셀 다운로드
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDashboardReport() {
        DashboardExcelResult excelResult = adminDashboardExcelService.generateDashboardExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            String encodedFileName = URLEncoder.encode(excelResult.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);
        } catch (Exception e) {
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=default.xlsx");
        }

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(excelResult.getFileBytes());
    }
}
