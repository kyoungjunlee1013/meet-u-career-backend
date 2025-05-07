package com.highfive.meetu.domain.job.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ApplicantStatsDTO {
    private int totalApplicants;
    private int documentReviewing;
    private int documentPassed;
    private int documentFailed;
    private int interviewCompleted;
}
