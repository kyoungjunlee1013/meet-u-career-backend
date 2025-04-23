package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class MonthlyJobPostingCountDTO {
    private String month;
    private long jobPostingCount; // 해당 월의 채용공고 수
}
