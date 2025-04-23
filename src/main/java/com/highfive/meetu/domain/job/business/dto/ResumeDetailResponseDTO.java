package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import lombok.*;

/**
 * 이력서 상세 정보를 담는 DTO (기업회원이 지원자의 이력서를 열람할 때 사용)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDetailResponseDTO {

    private Long resumeId;           // 이력서 ID
    private String title;            // 이력서 제목
    private String overview;         // 간략 자기소개
    private String resumeFileKey;    // S3 저장된 이력서 파일 키
    private String resumeFileName;   // 업로드된 이력서 원본 파일명
    private String resumeFileType;   // 이력서 파일 MIME 타입
    private String resumeUrl;        // 외부 이력서 URL (resumeType이 URL인 경우)
    private String extraLink1;       // 추가 링크 1 (예: GitHub, 블로그 등)
    private String extraLink2;       // 추가 링크 2 (예: 포트폴리오 등)
    private Boolean isPrimary;       // 대표 이력서 여부
    private Integer resumeType;      // 이력서 유형 (직접입력, 파일업로드, URL 등록)
    private Integer status;          // 이력서 상태 (공개, 비공개 등)

    /**
     * Resume 엔티티를 ResumeDetailResponseDTO로 변환하는 정적 메서드
     *
     * @param resume 변환할 Resume 엔티티
     * @return 변환된 ResumeDetailResponseDTO 객체
     */
    public static ResumeDetailResponseDTO fromEntity(Resume resume) {
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
            .build();
    }
}
