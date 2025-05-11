package com.highfive.meetu.domain.job.personal.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponseDTO {
    private Integer total;
    private List<JobRecommendationDTO> recommendations;
}
