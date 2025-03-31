package com.highfive.meetu.domain.coverletter.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 자기소개서 항목 엔티티
 *
 * 연관관계:
 * - CoverLetterContent(N) : CoverLetter(1) - CoverLetterContent가 주인, @JoinColumn 사용
 */
@Entity(name = "coverLetterContent")
@Table(
        indexes = {
                @Index(name = "idx_coverLetterContent_coverLetterId", columnList = "coverLetterId"),
                @Index(name = "idx_coverLetterContent_contentOrder", columnList = "contentOrder")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"coverLetter"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CoverLetterContent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverLetterId", nullable = false)
    private CoverLetter coverLetter;  // 자기소개서 ID (외래 키)

    @Column(length = 255, nullable = false)
    private String sectionTitle;  // 항목 제목 (예: "지원 동기", "성장 과정")

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;  // 항목 내용

    @Column(nullable = false)
    private Integer contentOrder;  // 순서 (Drag & Drop 정렬 가능)
}