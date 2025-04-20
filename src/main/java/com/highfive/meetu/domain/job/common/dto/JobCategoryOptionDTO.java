package com.highfive.meetu.domain.job.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCategoryOptionDTO {

    private String value;   // jobCode
    private String label;   // jobName
}
