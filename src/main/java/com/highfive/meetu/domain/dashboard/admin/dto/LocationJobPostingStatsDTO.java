package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LocationJobPostingStatsDTO {
    private String locationName; // 지역명 (예: 서울특별시, 경기도 등)
    private Long jobPostingCount; // 해당 지역의 채용공고 수
}
