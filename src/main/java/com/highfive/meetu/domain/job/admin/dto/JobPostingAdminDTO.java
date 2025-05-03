package com.highfive.meetu.domain.job.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자용 채용공고 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingAdminDTO {

  private Long id;
  private String title;
  private String companyName;
  private String registrationDate;
  private String deadline;
  private Integer viewCount;
  private Integer applyCount;
  private String status;
  private String description;
}
