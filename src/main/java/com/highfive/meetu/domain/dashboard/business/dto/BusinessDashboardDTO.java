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

  private String companyName;
  private String industry;
  private String address;
  private String foundedDate;
  private String employeeScale;

  private int totalJobPostings;
  private int activeJobPostings;
  private int closedJobPostings;
  private int nearingDeadlineJobPostings;

  private int totalViews;
  private int totalApplications;

  private Map<String, Integer> jobCategoryViewCount;
  private Map<String, Integer> jobCategoryApplicationCount;

  private List<JobPostingSimpleDTO> jobPostings;

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
            .companyName(company.getName())
            .industry(company.getIndustry())
            .address(company.getAddress())
            .foundedDate(company.getFoundedDate() != null ? company.getFoundedDate().toString() : null)
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