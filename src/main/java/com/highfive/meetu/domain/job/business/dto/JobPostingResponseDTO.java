package com.highfive.meetu.domain.job.business.dto;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.*;

/**
 * [기업회원] 내 기업의 채용공고 목록 조회용 DTO
 * - 기업 관리자 페이지에서 채용공고 리스트를 불러올 때 사용
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingResponseDTO {

    private Long id;      // 채용공고 ID
    private String title; // 채용공고 제목

    /**
     * JobPosting 엔티티를 DTO로 변환하는 정적 메서드
     *
     * @param posting JobPosting 엔티티
     * @return JobPostingResponseDTO
     */
    public static JobPostingResponseDTO fromEntity(JobPosting posting) {
        return JobPostingResponseDTO.builder()
            .id(posting.getId())         // 채용공고 ID 설정
            .title(posting.getTitle())   // 채용공고 제목 설정
            .build();
    }
}
