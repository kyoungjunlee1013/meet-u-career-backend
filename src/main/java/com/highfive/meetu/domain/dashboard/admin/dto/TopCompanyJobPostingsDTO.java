package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class TopCompanyJobPostingsDTO {
    private String companyName;  // 회사명
    private long jobPostingCount;  // 채용공고 수
}
