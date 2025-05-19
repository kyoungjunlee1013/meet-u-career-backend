package com.highfive.meetu.domain.coverletter.personal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCoachingResponseDTO {

    private String feedback;
    private String revisedContent;
}