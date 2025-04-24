package com.highfive.meetu.domain.dashboard.business.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.dashboard.business.dto.CompanyProfileDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.dashboard.business.dto.BusinessDashboardDTO;
import com.highfive.meetu.domain.dashboard.business.dto.JobPostingSimpleDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessDashboardService {

  private final CompanyRepository companyRepository;
  private final JobPostingRepository jobPostingRepository;
  private final ApplicationRepository applicationRepository;

  public BusinessDashboardDTO getDashboard(Long companyId) {
    Company company = companyRepository.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기업입니다."));

    return buildDashboardDTO(company);
  }

  public BusinessDashboardDTO getDashboardByAccount(Account account) {
    Company company = account.getCompany();
    if (company == null) {
      throw new IllegalArgumentException("해당 계정에 연결된 기업이 없습니다.");
    }
    return buildDashboardDTO(company);
  }

  public CompanyProfileDTO getCompanyProfile(Account account) {
    Company company = account.getCompany();
    if (company == null) {
      throw new IllegalArgumentException("해당 계정에 연결된 기업이 없습니다.");
    }
    return CompanyProfileDTO.buildFromEntity(company);
  }

  private BusinessDashboardDTO buildDashboardDTO(Company company) {
    Long companyId = company.getId();
    List<JobPosting> postings = jobPostingRepository.findByCompanyId(companyId);
    int totalPostings = postings.size();
    int activePostings = (int) postings.stream().filter(jp -> jp.getStatus() == 2).count();
    int closedPostings = (int) postings.stream().filter(jp -> jp.getExpirationDate().isBefore(LocalDateTime.now())).count();
    int nearingDeadlinePostings = (int) postings.stream()
        .filter(jp -> {
          LocalDateTime now = LocalDateTime.now();
          return !jp.getExpirationDate().isBefore(now) &&
              jp.getExpirationDate().isBefore(now.plusDays(3));
        }).count();

    int totalViews = postings.stream().mapToInt(JobPosting::getViewCount).sum();

    Map<Long, Integer> applicationMap = applicationRepository.countApplicationsGroupByJobPosting(companyId);
    int totalApplications = applicationMap.values().stream().mapToInt(Integer::intValue).sum();

    Map<String, Integer> viewByCategory = new HashMap<>();
    Map<String, Integer> appByCategory = new HashMap<>();

    List<JobPostingSimpleDTO> jobPostingDTOs = postings.stream()
        .map(jp -> {
          String category = jp.getJobPostingJobCategoryList() != null && !jp.getJobPostingJobCategoryList().isEmpty()
              ? jp.getJobPostingJobCategoryList().get(0).getJobCategory().getJobName()
              : "기타";

          int apps = applicationMap.getOrDefault(jp.getId(), 0);

          viewByCategory.merge(category, jp.getViewCount(), Integer::sum);
          appByCategory.merge(category, apps, Integer::sum);

          return JobPostingSimpleDTO.from(jp, apps);
        }).collect(Collectors.toList());

    return BusinessDashboardDTO.build(
        company,
        totalPostings,
        activePostings,
        closedPostings,
        nearingDeadlinePostings,
        totalViews,
        totalApplications,
        viewByCategory,
        appByCategory,
        jobPostingDTOs
    );
  }
}