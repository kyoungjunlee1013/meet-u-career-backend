package com.highfive.meetu.domain.job.business.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {
    private String name;
    private String level;

    // ResumeContent로부터 생성될 경우 직접 매핑 필요
    public static LanguageDTO from(String name, String level) {
        return LanguageDTO.builder()
            .name(name)
            .level(level)
            .build();
    }
}