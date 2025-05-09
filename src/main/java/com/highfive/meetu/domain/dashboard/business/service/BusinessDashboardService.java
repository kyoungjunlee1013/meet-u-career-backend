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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessDashboardService {

  private final CompanyRepository companyRepository;
  private final JobPostingRepository jobPostingRepository;
  private final ApplicationRepository applicationRepository;
  private final AccountRepository accountRepository;

  public BusinessDashboardDTO getDashboard(Long accountId) {
    Company company = findCompanyByAccountId(accountId);
    return buildDashboardDTO(company);
  }

  public CompanyProfileDTO getCompanyProfile(Long accountId) {
    Company company = findCompanyByAccountId(accountId);
    return CompanyProfileDTO.buildFromEntity(company);
  }

  @Transactional
  public Long updateCompanyProfile(Long accountId, CompanyProfileDTO dto) {
    Company company = findCompanyByAccountId(accountId);

    company.setName(dto.getCompanyName());
    company.setWebsite(dto.getWebsite());
    company.setAddress(dto.getAddress());
    company.setIndustry(dto.getIndustry());
    company.setLogoKey(dto.getLogoKey());
    company.setRepresentativeName(dto.getRepresentativeName());
    company.setBusinessNumber(dto.getBusinessNumber());
    company.setNumEmployees(dto.getNumEmployees() != null ? dto.getNumEmployees() : 0);
    company.setRevenue(dto.getRevenue() != null ? dto.getRevenue() : 0L);
    company.setFoundedDate(
            dto.getFoundedDate() != null && !dto.getFoundedDate().isBlank()
                    ? LocalDate.parse(dto.getFoundedDate())
                    : null
    );

    Company savedCompany = companyRepository.save(company);
    System.out.println("✅ 저장된 company ID = " + savedCompany.getId());
    return savedCompany.getId();
  }

  private Company findCompanyByAccountId(Long accountId) {
    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));
    Company company = account.getCompany();
    if (company == null) {
      throw new NotFoundException("해당 계정에 연결된 기업이 없습니다.");
    }
    return company;
  }

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

    // ✅ 여기 핵심: 공고별 Map 제거하고 회사 전체 지원자 수 직접 계산
    int totalApplications = applicationRepository.countTotalApplicationsByCompanyId(companyId);

    Map<String, Integer> viewByCategory = new HashMap<>();
    Map<String, Integer> appByCategory = new HashMap<>();

    List<JobPostingSimpleDTO> jobPostingDTOs = postings.stream()
            .map(jp -> {
              String category = (jp.getJobPostingJobCategoryList() != null && !jp.getJobPostingJobCategoryList().isEmpty())
                      ? jp.getJobPostingJobCategoryList().get(0).getJobCategory().getJobName()
                      : "기타";

              int apps = 0;

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
