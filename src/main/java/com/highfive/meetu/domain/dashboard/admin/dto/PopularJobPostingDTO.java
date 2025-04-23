package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class PopularJobPostingDTO {
    private String jobTitle;       // 채용공고 제목
    private String companyName;    // 회사명
    private long applicantCount;  // 지원자 수
}
