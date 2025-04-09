package com.highfive.meetu.domain.application.personal.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCompanySummaryDTO {

  private Long companyId;
  private String companyName;
  private Long reviewCount;

  private String industry;
  private String logoUrl;
  private String address;

  /**
   * 정적 생성자 (Entity 기반 변환)
   */
  public static InterviewCompanySummaryDTO of(Company company, Long reviewCount) {
    return InterviewCompanySummaryDTO.builder()
        .companyId(company.getId())
        .companyName(company.getName())
        .reviewCount(reviewCount)
        .industry(company.getIndustry())
        .logoUrl(company.getLogoKey())
        .address(company.getAddress())
        .build();
  }
}
