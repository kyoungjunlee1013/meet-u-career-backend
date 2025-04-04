package com.highfive.meetu.domain.resume.personal.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 이력서 DTO (개인 회원용)
 * - 목록 조회, 상세 조회, 작성, 수정에 공통 사용
 * - 상태/이력서유형 등은 Entity 내 상수를 참조
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumePersonalDTO {

    // 기본 정보 (목록 조회 시 필요)
    private Long id;                    // 이력서 ID
    private String title;               // 이력서 제목
    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime updatedAt;    // 수정일
    private Integer status;             // 상태 코드

    // 목록 조회 화면에서 필요한 추가 정보
    private String overview;            // 간단 소개
    private Integer resumeType;         // 이력서 유형 코드
    private Long coverLetterId;         // 연결된 자기소개서 ID
    private String coverLetterTitle;    // 연결된 자기소개서 제목

    // 이력서 상세정보 (상세 조회/작성/수정 시 필요)
    private String resumeFile;          // 이력서 파일 경로
    private String resumeUrl;           // 이력서 URL
    private String extraLink1;          // 추가 링크 1 (GitHub, 포트폴리오 등)
    private String extraLink2;          // 추가 링크 2 (LinkedIn, 블로그 등)

    // 프로필 정보
    private Long profileId;             // 프로필 ID

    // 이력서 항목 리스트
    private List<ResumeContentPersonalDTO> contents;
}
