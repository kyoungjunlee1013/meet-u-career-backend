package com.highfive.meetu.domain.job.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCategoryOptionDTO {

    private Long id;        // 고유 식별자(PK)
    private String value;   // jobCode
    private String label;   // jobName
}
