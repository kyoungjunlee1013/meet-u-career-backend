package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

import java.util.List;

/**
 * 이력서 작성 요청 DTO (이력서 + 항목 리스트 + 프로필 일부 수정 포함)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeSaveRequestDTO {

    private ResumeInfo resume;                     // 기존 resume 정보(타이틀, 타입 등)
    private ProfileUpdateDTO profile;              // 프로필 수정 정보
    private List<ResumeContentSaveDTO> resumeContents; // 이력서 항목 리스트

    // 내부 static 클래스로 Resume 정보 받기
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResumeInfo {
        private String title;
        private Integer resumeType;
        private String resumeFileKey;
        private String resumeFileName;
        private String resumeFileType;
        private String resumeUrl;
        private String overview;
        private Long coverLetterId;
        private String extraLink1;
        private String extraLink2;
        private Boolean isPrimary;
        private Integer status;  // PRIVATE, PUBLIC, 등
    }

    public Resume toEntity(Profile profile) {
        return Resume.builder()
            .profile(profile)
            .title(this.resume.getTitle())
            .resumeType(this.resume.getResumeType())
            .resumeFileKey(this.resume.getResumeFileKey())
            .resumeFileName(this.resume.getResumeFileName())
            .resumeFileType(this.resume.getResumeFileType())
            .resumeUrl(this.resume.getResumeUrl())
            .overview(this.resume.getOverview())
            .extraLink1(this.resume.getExtraLink1())
            .extraLink2(this.resume.getExtraLink2())
            .isPrimary(this.resume.getIsPrimary() != null ? this.resume.getIsPrimary() : false)
            .status(this.resume.getStatus() != null ? this.resume.getStatus() : Resume.Status.DRAFT)
            .build();
    }

}