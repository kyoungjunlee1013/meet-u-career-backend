package com.highfive.meetu.domain.coverletter.personal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCoachingRequestDTO {

    private String sectionTitle;

    private String content;
}