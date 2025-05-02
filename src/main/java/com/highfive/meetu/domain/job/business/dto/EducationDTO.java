package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {
    private String school;
    private String degree;
    private String field;
    private String gpa;
    private String period;

    public static EducationDTO from(ResumeContent c) {
        return EducationDTO.builder()
            .school(c.getOrganization())
            .degree(c.getTitle())
            .field(c.getField())
            .gpa(extractGPA(c.getDescription()))
            .period(formatPeriod(c.getDateFrom(), c.getDateTo()))
            .build();
    }

    private static String extractGPA(String description) {
        // 예: "학점: 3.8/4.5" 이 포함된 description에서 학점만 추출
        if (description == null) return null;

        // 정규표현식으로 "학점: x.x/x.x" 패턴을 찾음
        Pattern pattern = Pattern.compile("학점[:：]\\s*([0-9.]+/[0-9.]+)");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            return matcher.group(1);  // 예: "3.8/4.5"
        }

        return null;
    }

    private static String formatPeriod(LocalDate from, LocalDate to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        String start = (from != null) ? from.format(formatter) : "미입력";
        String end = (to != null) ? to.format(formatter) : "현재";
        return start + " - " + end;
    }
}