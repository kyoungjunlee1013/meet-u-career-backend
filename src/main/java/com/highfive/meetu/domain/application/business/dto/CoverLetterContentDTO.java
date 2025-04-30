package com.highfive.meetu.domain.application.business.dto;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetterContent;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverLetterContentDTO {
    private String sectionTitle;
    private String content;

    public static CoverLetterContentDTO from(CoverLetterContent entity) {
        return CoverLetterContentDTO.builder()
            .sectionTitle(entity.getSectionTitle())
            .content(entity.getContent())
            .build();
    }
}
