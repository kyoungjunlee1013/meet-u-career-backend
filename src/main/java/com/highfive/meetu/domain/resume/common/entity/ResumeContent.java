package com.highfive.meetu.domain.resume.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.resume.common.type.ResumeContentTypes.SectionType;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 이력서 항목 엔티티
 *
 * 연관관계:
 * - ResumeContent(N) : Resume(1) - ResumeContent가 주인, @JoinColumn 사용
 */
@Entity(name = "resumeContent")
@Table(
        indexes = {
                @Index(name = "idx_resumeContent_resumeId", columnList = "resumeId"),
                @Index(name = "idx_resumeContent_sectionType", columnList = "sectionType"),
                @Index(name = "idx_resumeContent_contentOrder", columnList = "contentOrder")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"resume"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResumeContent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resumeId", nullable = false)
    private Resume resume;  // 해당 이력서 ID (외래 키)

    @Column(nullable = false)
    private SectionType sectionType;  // 항목 유형 (EDUCATION, EXPERIENCE, CERTIFICATE, ACTIVITY) - 컨버터 자동 적용

    @Column(length = 255, nullable = false)
    private String sectionTitle;  // 항목 제목 ("경력", "학력" 등)

    @Column(nullable = false)
    private Integer contentOrder;  // 항목 순서 (낮을수록 상단 표시)

    @Column(length = 255, nullable = false)
    private String organization;  // 기관/기업명/발급기관 (예: "서울대학교", "삼성전자", "한국산업인력공단")

    @Column(length = 255, nullable = false)
    private String title;  // 직책, 학위명 또는 자격증명 (예: "백엔드 개발자", "정보처리기사")

    @Column(length = 255, nullable = false)
    private String field;  // 전공, 직무 분야 (예: "컴퓨터공학"), 관련 분야 (예: "IT", "금융", "교육")

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;  // 상세 설명

    @Column
    private LocalDate dateFrom;  // 시작일 또는 발급일 (자격증일 경우 이 컬럼 사용)

    @Column
    private LocalDate dateTo;  // 종료일 (자격증이면 NULL)

    /**
     * 현재 진행 중인지 여부 (종료일이 없으면 현재 진행 중)
     */
    @Transient
    public boolean isOngoing() {
        return dateTo == null;
    }

    /**
     * 항목 기간 표시 (예: "2020.03 - 2023.02" 또는 "2020.03 - 현재")
     */
    @Transient
    public String getFormattedPeriod() {
        String from = dateFrom == null ? "" : dateFrom.toString().substring(0, 7).replace('-', '.');
        String to = isOngoing() ? "현재" : dateTo.toString().substring(0, 7).replace('-', '.');
        return from + " - " + to;
    }
}