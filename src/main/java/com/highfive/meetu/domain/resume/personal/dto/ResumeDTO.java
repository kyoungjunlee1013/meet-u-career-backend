package com.highfive.meetu.domain.resume.personal.dto;

import com.highfive.meetu.domain.resume.common.entity.Resume;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDTO {

    private Long id;                // 수정 시 사용
    private String title;           // 이력서 제목
    private Integer resumeType;     // 이력서 유형 (직접 입력, 파일, URL)
    private String resumeFileKey;   // S3 저장 키
    private String resumeFileName;  // 원본 파일명
    private String resumeFileType;  // 파일 타입
    private String resumeUrl;       // URL형 이력서
    private String overview;        // 자기소개
    private String extraLink1;      // 추가 링크 1
    private String extraLink2;      // 추가 링크 2
    private Integer status;         // 상태값 (임시저장, 비공개, 공개)

    // 필요한 경우, 프론트에서 받아와서 Service 단에서 처리
    private Boolean isPrimary;      // 대표 이력서 여부 (기본값 false로 처리)

    public Resume toEntity() {
        return Resume.builder()
                .id(id)
                .title(title)
                .resumeType(resumeType)
                .resumeFileKey(resumeFileKey)
                .resumeFileName(resumeFileName)
                .resumeFileType(resumeFileType)
                .resumeUrl(resumeUrl)
                .overview(overview)
                .extraLink1(extraLink1)
                .extraLink2(extraLink2)
                .status(status)
                .isPrimary(isPrimary != null ? isPrimary : false) // 기본값 처리
                .build();
    }
}

