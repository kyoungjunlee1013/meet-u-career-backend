package com.highfive.meetu.domain.coverletter.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 자기소개서 항목 피드백 엔티티
 *
 * 연관관계:
 * - CoverLetterContentFeedback(N) : CoverLetterContent(1)
 * - CoverLetterContentFeedback이 주인, contentId를 외래키로 가짐
 */
@Entity(name = "coverLetterContentFeedback")
@Table(
        indexes = {
                @Index(name = "idx_coverLetterContentFeedback_contentId", columnList = "contentId")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"content"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CoverLetterContentFeedback extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)  // OneToOne에서 ManyToOne으로 변경 (1:N 관계)
    @JoinColumn(name = "contentId", nullable = false)  // unique = true 제거
    private CoverLetterContent content;  // 자기소개서 항목 ID (1:N 관계, 외래 키)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originalContent;  // 피드백 요청 시점의 원본 내용

    @Column(columnDefinition = "TEXT", nullable = false)
    private String feedback;  // 피드백 문장

    @Column(columnDefinition = "TEXT", nullable = false)
    private String revisedContent;   // 수정된 자기소개서 내용

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isApplied;   // 피드백 적용 여부

    @Column
    private LocalDateTime appliedAt;  // 피드백 적용 시점

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 항목 수정일
}