package com.highfive.meetu.domain.resume.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 이력서 엔티티
 *
 * 연관관계:
 * - Profile(1) : Resume(N) - Resume가 주인, @JoinColumn 사용
 * - CoverLetter(1) : Resume(1) - Resume가 주인, @JoinColumn 사용
 * - Resume(1) : ResumeContent(N) - Resume가 비주인, mappedBy 사용
 */
@Entity(name = "resume")
@Table(
        indexes = {
                @Index(name = "idx_resume_profileId", columnList = "profileId"),
                @Index(name = "idx_resume_status", columnList = "status"),
                @Index(name = "idx_resume_isPrimary", columnList = "isPrimary")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"profile", "coverLetter", "resumeContentList"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Resume extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;  // 구직자 프로필 (FK)

    @Column(length = 255, nullable = false)
    private String title;  // 이력서 제목

    @Column(nullable = false)
    private Integer resumeType;  // 이력서 유형 (CUSTOM, FILE, URL)

    @Column(length = 500)
    private String resumeFileKey; // S3 Key (예: resume/file_xxx.pdf)

    @Column(length = 255)
    private String resumeFileName; // 원본 파일명 (예: 홍길동_이력서.pdf)

    @Column(length = 100)
    private String resumeFileType; // 파일 타입 (예: application/pdf)


    @Column(length = 500)
    private String resumeUrl;  // 이력서 URL (resumeType = URL일 경우)

    @Column(columnDefinition = "TEXT")
    private String overview;  // 간략한 자기소개

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverLetterId")
    private CoverLetter coverLetter;  // 자기소개서 (FK)

    @Column(length = 500)
    private String extraLink1;  // 추가 링크 1 (GitHub, Blog, LinkedIn 등)

    @Column(length = 500)
    private String extraLink2;  // 추가 링크 2 (GitHub, Blog, LinkedIn 등)

    @Column(nullable = false)
    private Boolean isPrimary;  // 대표 이력서 여부

    @Column(nullable = false)
    private Integer status;  // 이력서 상태 (DRAFT, PRIVATE, PUBLIC, DELETED)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 이력서 수정일

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "resume",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"resume"})
    @Builder.Default
    private List<ResumeContent> resumeContentList = new ArrayList<>();

    /**
     * 이력서 유형
     */
    public static class ResumeType {
        public static final int DIRECT_INPUT = 0;  // 직접 입력
        public static final int FILE_UPLOAD = 1;   // 파일 업로드
        public static final int URL = 2;           // 외부 URL 등록
    }

    /**
     * 상태값
     */
    public static class Status {
        public static final int DRAFT = 0;    // 임시저장
        public static final int PRIVATE = 1;  // 비공개 등록
        public static final int PUBLIC = 2;   // 공개 등록
        public static final int DELETED = 3;  // 삭제됨
    }
}