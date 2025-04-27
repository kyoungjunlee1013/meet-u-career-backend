package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class JobPostingStatisticsDTO {
    private long activeCount;
    private long expiredCount;
    private long draftCount;
    private double averageViewCount;
    private double averageApplyCount;
    private double averagePostingDays;
}
