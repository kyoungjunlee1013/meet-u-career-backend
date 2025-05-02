package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO {
    private String name;
    private String date;

    public static CertificateDTO from(ResumeContent c) {
        return CertificateDTO.builder()
            .name(c.getTitle()) // 자격증명
            .date(formatDate(c.getDateFrom())) // 발급일
            .build();
    }

    private static String formatDate(LocalDate date) {
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy.MM")) : "미입력";
    }
}
