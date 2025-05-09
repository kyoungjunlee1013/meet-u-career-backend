package com.highfive.meetu.domain.dashboard.business.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDashboardDTO {

  // ✅ 회사 기본 정보
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

  // ✅ 추가 정보
  private String employeeScale; // 문자열 예: "50명"

  // ✅ 대시보드 통계
  private int totalJobPostings;
  private int activeJobPostings;
  private int closedJobPostings;
  private int nearingDeadlineJobPostings;

  private int totalViews;
  private int totalApplications;

  // ✅ 카테고리별 통계
  private Map<String, Integer> jobCategoryViewCount;
  private Map<String, Integer> jobCategoryApplicationCount;

  // ✅ 공고 리스트
  private List<JobPostingSimpleDTO> jobPostings;

  // ✅ 정적 빌더 메서드
  public static BusinessDashboardDTO build(Company company,
                                           int total,
                                           int active,
                                           int closed,
                                           int nearingDeadline,
                                           int totalViews,
                                           int totalApps,
                                           Map<String, Integer> viewMap,
                                           Map<String, Integer> appMap,
                                           List<JobPostingSimpleDTO> postings) {
    return BusinessDashboardDTO.builder()
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
            .employeeScale(company.getNumEmployees() + "명")

            .totalJobPostings(total)
            .activeJobPostings(active)
            .closedJobPostings(closed)
            .nearingDeadlineJobPostings(nearingDeadline)
            .totalViews(totalViews)
            .totalApplications(totalApps)

            .jobCategoryViewCount(viewMap)
            .jobCategoryApplicationCount(appMap)
            .jobPostings(postings)

            .build();
  }
}
