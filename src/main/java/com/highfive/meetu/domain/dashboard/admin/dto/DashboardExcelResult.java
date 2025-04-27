package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

/**
 * 대시보드 엑셀 생성 결과 DTO
 * (파일 이름 + 파일 데이터)
 */
@Getter
@AllArgsConstructor
public class DashboardExcelResult {
    private String fileName; // 생성된 파일 이름
    private final byte[] fileBytes; // 파일 데이터 (byte 배열)
}