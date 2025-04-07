package com.highfive.meetu.domain.coverletter.personal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachingResponseDTO {
    private Long contentId;
    private Long feedbackId;
    private String feedback;
    private String revisedContent;
    private String originalContent;
    private Boolean isApplied;
    private LocalDateTime appliedAt;
    private LocalDateTime createdAt;

    // 기존 생성자 유지 (하위 호환성)
    public CoachingResponseDTO(Long contentId, String feedback, String revisedContent) {
        this.contentId = contentId;
        this.feedback = feedback;
        this.revisedContent = revisedContent;
        this.isApplied = false;
    }
}
