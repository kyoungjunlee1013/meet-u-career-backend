package com.highfive.meetu.domain.coverletter.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.coverletter.common.type.CoverLetterTypes.Status;
import com.highfive.meetu.domain.resume.common.entity.Resume;
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
 * 자기소개서 엔티티
 *
 * 연관관계:
 * - CoverLetter(N) : Profile(1) - CoverLetter가 주인, @JoinColumn 사용
 * - CoverLetter(1) : Resume(1) - CoverLetter가 비주인, mappedBy 사용
 * - CoverLetter(1) : CoverLetterContent(N) - CoverLetter가 비주인, mappedBy 사용
 */
@Entity(name = "coverLetter")
@Table(
        indexes = {
                @Index(name = "idx_coverLetter_profileId", columnList = "profileId"),
                @Index(name = "idx_coverLetter_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"profile", "resume", "coverLetterContentList"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CoverLetter extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;  // 구직자 프로필 ID

    @Column(length = 255, nullable = false)
    private String title;  // 자기소개서 제목

    @Column(nullable = false)
    private Status status;  // 자기소개서 상태 (ACTIVE, DRAFT, DELETED) - 컨버터 자동 적용

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 자기소개서 수정일

    @OneToOne(mappedBy = "coverLetter", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"coverLetter"})
    private Resume resume;  // 연결된 이력서

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "coverLetter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"coverLetter"})
    @Builder.Default
    private List<CoverLetterContent> coverLetterContentList = new ArrayList<>();

    /**
     * 자기소개서 항목 추가 편의 메서드
     */
    public void addContent(CoverLetterContent content) {
        this.coverLetterContentList.add(content);
        content.setCoverLetter(this);
    }

    /**
     * 자기소개서 항목 제거 편의 메서드
     */
    public void removeContent(CoverLetterContent content) {
        this.coverLetterContentList.remove(content);
        content.setCoverLetter(null);
    }

    /**
     * 자기소개서 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 자기소개서가 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }
}