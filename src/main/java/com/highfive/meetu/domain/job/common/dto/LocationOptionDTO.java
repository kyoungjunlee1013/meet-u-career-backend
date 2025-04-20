package com.highfive.meetu.domain.job.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationOptionDTO {

    private String value;   // locationCode
    private String label; // province
}
