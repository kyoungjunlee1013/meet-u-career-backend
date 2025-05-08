package com.highfive.meetu.domain.dashboard.business.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileDTO {
  private Long companyId;
  private String companyName;
  private String industry;
  private String address;
  private String foundedDate;
  private String website;
  private String logoKey;
  private Integer numEmployees;
  private Long revenue;
  private String representativeName;
  private String businessNumber;
  private Integer status;
  private String updatedAt;

  public static CompanyProfileDTO buildFromEntity(Company company) {
    return CompanyProfileDTO.builder()
            .companyId(company.getId())
            .companyName(company.getName())
            .industry(company.getIndustry())
            .address(company.getAddress())
            .foundedDate(company.getFoundedDate() != null ? company.getFoundedDate().toString() : null)
            .website(company.getWebsite())
            .logoKey(company.getLogoKey())
            .numEmployees(company.getNumEmployees())
            .revenue(company.getRevenue())
            .representativeName(company.getRepresentativeName())
            .businessNumber(company.getBusinessNumber())
            .status(company.getStatus())
            .updatedAt(company.getUpdatedAt() != null ? company.getUpdatedAt().toString() : null)
            .build();
  }
}