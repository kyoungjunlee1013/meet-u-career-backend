package com.highfive.meetu.domain.coverletter.personal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachingRequestDTO {
    private Long contentId;
    private String sectionTitle;
    private String content;
}
