package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import lombok.*;

import java.util.List;

/**
 * 이력서 상세 정보를 담는 DTO (기업회원이 지원자의 이력서를 열람할 때 사용)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDetailResponseDTO {

    // 기본 이력서 정보
    private Long resumeId;
    private String title;
    private String overview;
    private String resumeFileKey;
    private String resumeFileName;
    private String resumeFileType;
    private String resumeUrl;
    private String extraLink1;
    private String extraLink2;
    private Boolean isPrimary;
    private Integer resumeType;
    private Integer status;

    // 상세 이력서 항목
    private List<EducationDTO> educations;
    private List<ExperienceDTO> experiences;
    private List<ProjectDTO> projects;
    private List<String> skills;
    private List<LanguageDTO> languages;
    private List<CertificateDTO> certificates;

    /**
     * Resume + 상세 항목들을 포함하는 변환 메서드
     */
    public static ResumeDetailResponseDTO fromEntity(
        Resume resume,
        List<EducationDTO> educations,
        List<ExperienceDTO> experiences,
        List<ProjectDTO> projects,
        List<String> skills,
        List<LanguageDTO> languages,
        List<CertificateDTO> certificates
    ) {
        return ResumeDetailResponseDTO.builder()
            .resumeId(resume.getId())
            .title(resume.getTitle())
            .overview(resume.getOverview())
            .resumeFileKey(resume.getResumeFileKey())
            .resumeFileName(resume.getResumeFileName())
            .resumeFileType(resume.getResumeFileType())
            .resumeUrl(resume.getResumeUrl())
            .extraLink1(resume.getExtraLink1())
            .extraLink2(resume.getExtraLink2())
            .isPrimary(resume.getIsPrimary())
            .resumeType(resume.getResumeType())
            .status(resume.getStatus())
            .educations(educations)
            .experiences(experiences)
            .projects(projects)
            .skills(skills)
            .languages(languages)
            .certificates(certificates)
            .build();
    }
}
