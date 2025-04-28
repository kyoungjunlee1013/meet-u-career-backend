package com.highfive.meetu.domain.resume.personal.dto;

import lombok.*;

@Getter
@Setter
public class ApplyRequestDTO {

    private Long jobPostingId;  // 채용 공고 ID
    private Long resumeId;      // 지원할 이력서 ID

    public ApplyRequestDTO() {
        // 기본 생성자 (필수: JSON 매핑용)
    }

    public ApplyRequestDTO(Long jobPostingId, Long resumeId) {
        this.jobPostingId = jobPostingId;
        this.resumeId = resumeId;
    }
}