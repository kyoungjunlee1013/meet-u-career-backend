package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class PopularKeywordJobPostingsDTO {
    private String keyword;
    private long count;
}