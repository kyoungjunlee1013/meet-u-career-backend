package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyApplicationStatsDTO {
    LocalDate date;
    long totalApplications;
    long acceptedApplications;
    long rejectedApplication;
}
