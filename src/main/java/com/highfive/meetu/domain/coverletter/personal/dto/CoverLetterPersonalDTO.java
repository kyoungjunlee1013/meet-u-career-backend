package com.highfive.meetu.domain.coverletter.personal.dto;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoverLetterPersonalDTO {

    private Long id;                // 자기소개서 ID
    private String title;           // 제목
    private LocalDateTime updatedAt; // 수정일
    private Integer contentCount;   // 항목 개수

    public static CoverLetterPersonalDTO fromEntity(CoverLetter entity) {
        return CoverLetterPersonalDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .updatedAt(entity.getUpdatedAt())
                .contentCount(entity.getCoverLetterContentList() != null
                        ? entity.getCoverLetterContentList().size()
                        : 0)
                .build();
    }
}
