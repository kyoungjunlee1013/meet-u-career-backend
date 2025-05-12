package com.highfive.meetu.domain.dashboard.business.dto;

import com.highfive.meetu.domain.job.common.entity.JobPosting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingSimpleDTO {

  private Long jobPostingId;
  private String title;
  private String location;
  private String postedDate;
  private String deadline;
  private int viewCount;
  private int applicationCount; // ✅ 공고별 지원자 수

  private String industry;
  private String jobType;
  private String salaryRange;
  private String keyword;

  public static JobPostingSimpleDTO from(JobPosting jp, int appCount) {
    return JobPostingSimpleDTO.builder()
            .jobPostingId(jp.getId())
            .title(jp.getTitle())
            .location(jp.getLocation().getFullLocation())
            .postedDate(jp.getPostingDate().toLocalDate().toString())
            .deadline(jp.getExpirationDate().toLocalDate().toString())
            .viewCount(jp.getViewCount())
            .applicationCount(appCount) // ✅ 지원자 수 반영
            .industry(jp.getIndustry())
            .jobType(jp.getJobType())
            .salaryRange(jp.getSalaryRange())
            .keyword(jp.getKeyword())
            .build();
  }
}
