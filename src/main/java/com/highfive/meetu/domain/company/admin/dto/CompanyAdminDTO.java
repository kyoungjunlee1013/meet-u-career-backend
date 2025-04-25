package com.highfive.meetu.domain.company.admin.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyAdminDTO {

  private Long id;
  private String name;
  private String businessNumber;
  private String industry;
  private LocalDate foundedDate;
  private int numEmployees;
  private long revenue;
  private String website;
  private String address;
  private String statusName;
  private LocalDate createdAt;

  private int jobPostingCount;
  private int managerCount;

  public static CompanyAdminDTO build(Company company, int jobPostingCount, int managerCount) {
    return CompanyAdminDTO.builder()
        .id(company.getId())
        .name(company.getName())
        .businessNumber(company.getBusinessNumber())
        .industry(company.getIndustry())
        .foundedDate(company.getFoundedDate())
        .numEmployees(company.getNumEmployees())
        .revenue(company.getRevenue())
        .website(company.getWebsite())
        .address(company.getAddress())
        .statusName(getStatusName(company.getStatus()))
        .createdAt(company.getCreatedAt().toLocalDate())
        .jobPostingCount(jobPostingCount)
        .managerCount(managerCount)
        .build();
  }

  private static String getStatusName(int status) {
    return switch (status) {
      case 0 -> "대기";
      case 1 -> "활성";
      case 2 -> "비활성";
      default -> "대기";
    };
  }
}
