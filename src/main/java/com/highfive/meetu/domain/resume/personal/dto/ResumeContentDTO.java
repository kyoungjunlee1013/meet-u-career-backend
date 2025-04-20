package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeContentDTO {

    private Long resumeId; // FK로 필요한 필드

    private Long id;
    private Integer sectionType;
    private String sectionTitle;
    private Integer contentOrder;

    private String organization;
    private String title;
    private String field;
    private String description;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    // 파일 정보 (S3 업로드 후 주입)
    private String contentFileKey;
    private String contentFileName;
    private String contentFileType;

    public ResumeContent toEntity() {
        return ResumeContent.builder()
                .id(id)
                .sectionType(sectionType)
                .sectionTitle(sectionTitle)
                .contentOrder(contentOrder)
                .organization(organization)
                .title(title)
                .field(field)
                .description(description)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .contentFileKey(contentFileKey)
                .contentFileName(contentFileName)
                .contentFileType(contentFileType)
                .build();
    }
}
