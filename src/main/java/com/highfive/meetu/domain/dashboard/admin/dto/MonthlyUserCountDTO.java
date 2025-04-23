package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class MonthlyUserCountDTO {
    private String month;
    private long userCount;
}
