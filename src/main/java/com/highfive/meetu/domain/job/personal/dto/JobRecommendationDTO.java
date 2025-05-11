package com.highfive.meetu.domain.job.personal.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobRecommendationDTO {
    private Long jobPostingId;
    private String title;
    private String description;
    private Integer score;
    private String industry;
    private String salaryRange;
    private LocalDate expirationDate;
    private CompanyDTO company;
}
