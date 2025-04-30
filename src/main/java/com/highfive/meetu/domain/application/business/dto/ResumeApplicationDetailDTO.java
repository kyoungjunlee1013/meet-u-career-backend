package com.highfive.meetu.domain.application.business.dto;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeApplicationDetailDTO {

    // 기본 정보
    private Long applicationId;
    private String applicantName;
    private String email;
    private String phoneNumber;
    private String position;          // 지원 포지션 (jobPosting.title)
    private String appliedDate;       // 지원일
    private Integer applicationStatus;

    private String profileImageUrl;

    // 이력서 정보
    private Long resumeId;
    private String resumeTitle;
    private String overview;
    private String resumeTypeLabel;
    private String resumeFileName;
    private String resumeFileKey;
    private String resumeFileType;
    private String resumeFileUrl;     // presigned URL
    private String resumeUrl;
    private String extraLink1;
    private String extraLink2;

    private List<ResumeContentDTO> resumeContents;

    // 자기소개서
    private String coverLetterTitle;
    private List<CoverLetterContentDTO> coverLetterContents;

    public static ResumeApplicationDetailDTO from(Application application, String presignedUrl) {
        Profile profile = application.getProfile();
        Resume resume = application.getResume();
        CoverLetter coverLetter = resume.getCoverLetter();

        return ResumeApplicationDetailDTO.builder()
            .applicationId(application.getId())
            .applicantName(profile.getAccount().getName())
            .email(profile.getAccount().getEmail())
            .phoneNumber(profile.getAccount().getPhone())
            .position(application.getJobPosting().getTitle())
            .appliedDate(application.getCreatedAt().toLocalDate().toString())
            .applicationStatus(application.getStatus())
            .profileImageUrl(profile.getProfileImageKey())

            .resumeId(resume.getId())
            .resumeTitle(resume.getTitle())
            .overview(resume.getOverview())
            .resumeTypeLabel(getResumeTypeLabel(resume.getResumeType()))
            .resumeFileName(resume.getResumeFileName())
            .resumeFileKey(resume.getResumeFileKey())
            .resumeFileType(resume.getResumeFileType())
            .resumeFileUrl(presignedUrl)
            .resumeUrl(resume.getResumeUrl())
            .extraLink1(resume.getExtraLink1())
            .extraLink2(resume.getExtraLink2())

            .resumeContents(resume.getResumeContentList().stream()
                .map(ResumeContentDTO::from)
                .collect(Collectors.toList()))

            .coverLetterTitle(coverLetter != null ? coverLetter.getTitle() : null)
            .coverLetterContents(coverLetter != null ?
                coverLetter.getCoverLetterContentList().stream()
                    .map(CoverLetterContentDTO::from)
                    .collect(Collectors.toList()) : List.of())

            .build();
    }

    private static String getResumeTypeLabel(int type) {
        return switch (type) {
            case 0 -> "직접입력";
            case 1 -> "파일";
            case 2 -> "URL";
            default -> "기타";
        };
    }
}
