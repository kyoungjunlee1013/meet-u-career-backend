package com.highfive.meetu.domain.job.business.dto;

import lombok.*;

@Getter
@Setter
public class UpdateApplicationStatusRequest {
    private Integer status; // 0~4
}