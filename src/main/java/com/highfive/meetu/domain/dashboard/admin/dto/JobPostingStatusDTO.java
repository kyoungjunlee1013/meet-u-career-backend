package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class JobPostingStatusDTO {
    private String statusName;  // 채용공고 상태 (활성, 마감, 임시저장)
    private long jobPostingCount;  // 해당 상태의 채용공고 수
    private double percentage;  // 상태 비율
}
