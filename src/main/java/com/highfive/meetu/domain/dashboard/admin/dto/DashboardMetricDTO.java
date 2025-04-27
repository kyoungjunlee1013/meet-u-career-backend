package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class DashboardMetricDTO {
    private long current; // 현재 값 (예: 현재 지원 건수)
    private long previous; // 이전 값 (예: 이전 지원 건수)
    private double growthRate; // 증가율
}