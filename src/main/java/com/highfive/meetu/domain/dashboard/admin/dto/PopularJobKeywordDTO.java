package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class PopularJobKeywordDTO {
    private String keyword;  // 키워드
    private long count;  // 채용공고 수
    private double growthRate;  // 증가율
}
