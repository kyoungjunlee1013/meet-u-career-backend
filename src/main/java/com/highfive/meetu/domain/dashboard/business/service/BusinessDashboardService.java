package com.highfive.meetu.domain.dashboard.business.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.dashboard.business.dto.BusinessDashboardDTO;
import com.highfive.meetu.domain.dashboard.business.dto.CompanyProfileDTO;
import com.highfive.meetu.domain.dashboard.business.dto.JobPostingSimpleDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
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
  private final AccountRepository accountRepository;

  /**
   * 기업 대시보드 데이터 조회
   */
  public BusinessDashboardDTO getDashboard(Long accountId) {
    Company company = findCompanyByAccountId(accountId);
    return buildDashboardDTO(company);
  }

  /**
   * 기업 프로필 정보 조회
   */
  public CompanyProfileDTO getCompanyProfile(Long accountId) {
    Company company = findCompanyByAccountId(accountId);
    return CompanyProfileDTO.buildFromEntity(company);
  }

  /**
   * 기업 프로필 수정
   */
  @Transactional
  public Long updateCompanyProfile(Long accountId, CompanyProfileDTO dto) {
    Company company = findCompanyByAccountId(accountId);

    company.setName(dto.getCompanyName());
    company.setWebsite(dto.getWebsite());
    company.setAddress(dto.getAddress());
    company.setIndustry(dto.getIndustry());
    company.setFoundedDate(dto.getFoundedDate() != null
            ? LocalDateTime.parse(dto.getFoundedDate()).toLocalDate() : null);
    company.setLogoKey(dto.getLogoKey());
    company.setRepresentativeName(dto.getRepresentativeName());
    company.setBusinessNumber(dto.getBusinessNumber());
    company.setNumEmployees(dto.getNumEmployees());
    company.setRevenue(dto.getRevenue());
    System.out.println("🔥 dto: " + dto); // 또는 dto.getCompanyName(), dto.getFoundedDate() 등
    System.out.println("🔥 [백엔드 저장됨] 회사명: " + company.getName()); // 꼭 넣자

    return company.getId();
  }


  /**
   * accountId를 기반으로 회사 찾기
   */
  private Company findCompanyByAccountId(Long accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));
    Company company = account.getCompany();
    if (company == null) {
      throw new NotFoundException("해당 계정에 연결된 기업이 없습니다.");
    }
    return company;
  }

  /**
   * 대시보드 데이터 조립
   */
  private BusinessDashboardDTO buildDashboardDTO(Company company) {
    Long companyId = company.getId();
    List<JobPosting> postings = jobPostingRepository.findByCompanyId(companyId);

    int totalPostings = postings.size();
    int activePostings = (int) postings.stream()
        .filter(jp -> jp.getStatus() == 2)
        .count();
    int closedPostings = (int) postings.stream()
        .filter(jp -> jp.getExpirationDate().isBefore(LocalDateTime.now()))
        .count();
    int nearingDeadlinePostings = (int) postings.stream()
        .filter(jp -> {
          LocalDateTime now = LocalDateTime.now();
          return !jp.getExpirationDate().isBefore(now) &&
              jp.getExpirationDate().isBefore(now.plusDays(3));
        })
        .count();
    int totalViews = postings.stream()
        .mapToInt(JobPosting::getViewCount)
        .sum();

    Map<Long, Integer> applicationMap = applicationRepository.countApplicationsGroupByJobPosting(companyId);
    int totalApplications = applicationMap.values().stream()
        .mapToInt(Integer::intValue)
        .sum();

    Map<String, Integer> viewByCategory = new HashMap<>();
    Map<String, Integer> appByCategory = new HashMap<>();

    List<JobPostingSimpleDTO> jobPostingDTOs = postings.stream()
        .map(jp -> {
          String category = (jp.getJobPostingJobCategoryList() != null && !jp.getJobPostingJobCategoryList().isEmpty())
              ? jp.getJobPostingJobCategoryList().get(0).getJobCategory().getJobName()
              : "기타";

          int apps = applicationMap.getOrDefault(jp.getId(), 0);

          viewByCategory.merge(category, jp.getViewCount(), Integer::sum);
          appByCategory.merge(category, apps, Integer::sum);

          return JobPostingSimpleDTO.from(jp, apps);
        })
        .collect(Collectors.toList());

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
