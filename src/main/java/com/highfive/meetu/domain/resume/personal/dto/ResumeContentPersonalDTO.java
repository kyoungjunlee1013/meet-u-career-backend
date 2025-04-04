package com.highfive.meetu.domain.resume.personal.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 이력서 항목 DTO (개인회원용)
 * - 이력서 작성/수정/조회 시 항목 리스트로 포함됨
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeContentPersonalDTO {

    private Long id;                // 항목 ID
    private Long resumeId;          // 이력서 ID
    private Integer sectionType;    // 항목 유형 코드
    private String sectionTitle;    // 항목 제목
    private Integer contentOrder;   // 항목 순서
    private String organization;    // 기관/기업명/발급기관
    private String title;           // 직책, 학위명 또는 자격증명
    private String field;           // 전공, 직무 분야, 관련 분야
    private String description;     // 상세 설명
    private LocalDateTime dateFrom; // 시작일 또는 발급일
    private LocalDateTime dateTo;   // 종료일 (자격증이면 NULL)
    private LocalDateTime createdAt;// 생성일
}
