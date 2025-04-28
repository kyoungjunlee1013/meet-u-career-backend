package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.entity.ResumeContent;
import lombok.*;

import java.time.LocalDate;

/**
 * 이력서 항목 저장용 DTO (ResumeContent)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeContentSaveDTO {
    private Integer sectionType;      // 항목 유형 (0:학력, 1:경력, 2:자격증, 3:활동, 4:포트폴리오 등)
    private String sectionTitle;      // 항목 제목
    private Integer contentOrder;     // 항목 순서
    private String organization;      // 기관명, 회사명, 학교명 등
    private String title;             // 직책명, 학위명, 자격증명 등
    private String field;             // 전공/직무 분야
    private String description;       // 상세 설명
    private LocalDate dateFrom;        // 시작일/발급일
    private LocalDate dateTo;          // 종료일 (자격증은 NULL)
    private String contentFileKey;    // 포트폴리오 파일 S3 Key
    private String contentFileName;   // 포트폴리오 파일명
    private String contentFileType;   // 포트폴리오 파일타입

    /**
     * DTO를 ResumeContent 엔티티로 변환
     */
    public ResumeContent toEntity(Resume resume) {
        return ResumeContent.builder()
            .resume(resume)
            .sectionType(this.sectionType)
            .sectionTitle(this.sectionTitle)
            .contentOrder(this.contentOrder)
            .organization(this.organization)
            .title(this.title)
            .field(this.field)
            .description(this.description)
            .dateFrom(this.dateFrom)
            .dateTo(this.dateTo)
            .contentFileKey(this.contentFileKey)
            .contentFileName(this.contentFileName)
            .contentFileType(this.contentFileType)
            .build();
    }

    public static ResumeContentSaveDTO fromEntity(ResumeContent entity) {
        return ResumeContentSaveDTO.builder()
            .sectionType(entity.getSectionType())
            .sectionTitle(entity.getSectionTitle())
            .contentOrder(entity.getContentOrder())
            .organization(entity.getOrganization())
            .title(entity.getTitle())
            .field(entity.getField())
            .description(entity.getDescription())
            .dateFrom(entity.getDateFrom())
            .dateTo(entity.getDateTo())
            .contentFileKey(entity.getContentFileKey())
            .contentFileName(entity.getContentFileName())
            .contentFileType(entity.getContentFileType())
            .build();
    }
}
