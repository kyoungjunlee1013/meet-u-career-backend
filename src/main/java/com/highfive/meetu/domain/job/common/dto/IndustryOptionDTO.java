package com.highfive.meetu.domain.job.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryOptionDTO {

    private String value;   // 산업 분야 값
    private String label;   // 산업 분야 표시명
}
