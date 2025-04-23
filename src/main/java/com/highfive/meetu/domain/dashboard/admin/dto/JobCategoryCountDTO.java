package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class JobCategoryCountDTO {
    private String categoryName;  // 직무명 (예: 개발, 마케팅 등)
    private long jobPostingCount;  // 해당 직무의 채용 공고 수
}