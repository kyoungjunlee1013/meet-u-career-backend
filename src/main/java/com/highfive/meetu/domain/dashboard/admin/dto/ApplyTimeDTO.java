package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class ApplyTimeDTO {
    private String timeRange; // 시간대 (예: "오전", "1-3시", "4-7시", etc.)
    private double completionTime; // 지원 완료 평균 시간
    private double cancellationTime; // 지원 취소 평균 시간
    private double modificationTime; // 지원 수정 평균 시간
}
