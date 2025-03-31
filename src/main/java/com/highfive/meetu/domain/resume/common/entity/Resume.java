package com.highfive.meetu.domain.resume.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.resume.common.type.ResumeTypes.Status;
import com.highfive.meetu.domain.resume.common.type.ResumeTypes.Type;
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
                @Index(name = "idx_resume_status", columnList = "status")
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
    private Type resumeType;  // 이력서 유형 (CUSTOM, FILE, URL) - 컨버터 자동 적용

    @Column(length = 500)
    private String resumeFile;  // 이력서 파일 첨부 (resumeType = FILE일 경우)

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
    private Status status;  // 이력서 상태 (ACTIVE, DRAFT, DELETED) - 컨버터 자동 적용

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
     * 이력서 항목을 추가하는 편의 메서드
     */
    public void addResumeContent(ResumeContent content) {
        this.resumeContentList.add(content);
        content.setResume(this);
    }

    /**
     * 이력서 항목을 제거하는 편의 메서드
     */
    public void removeResumeContent(ResumeContent content) {
        this.resumeContentList.remove(content);
        content.setResume(null);
    }

    /**
     * 이력서 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 이력서가 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }
}