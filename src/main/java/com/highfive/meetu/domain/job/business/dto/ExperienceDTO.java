package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
    private String company;
    private String position;
    private String description;
    private String period;

    public static ExperienceDTO from(ResumeContent c) {
        return ExperienceDTO.builder()
            .company(c.getOrganization())
            .position(c.getTitle())
            .description(c.getDescription())
            .period(formatPeriod(c.getDateFrom(), c.getDateTo()))
            .build();
    }

    private static String formatPeriod(LocalDate from, LocalDate to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        String start = (from != null) ? from.format(formatter) : "미입력";
        String end = (to != null) ? to.format(formatter) : "현재";
        return start + " - " + end;
    }
}

