package com.highfive.meetu.domain.application.business.dto;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeContentDTO {
    private String sectionTitle;
    private String organization;
    private String title;
    private String field;
    private String description;
    private String dateFrom;
    private String dateTo;

    public static ResumeContentDTO from(ResumeContent entity) {
        return ResumeContentDTO.builder()
            .sectionTitle(entity.getSectionTitle())
            .organization(entity.getOrganization())
            .title(entity.getTitle())
            .field(entity.getField())
            .description(entity.getDescription())
            .dateFrom(entity.getDateFrom() != null ? entity.getDateFrom().toString() : null)
            .dateTo(entity.getDateTo() != null ? entity.getDateTo().toString() : null)
            .build();
    }
}
