package com.highfive.meetu.domain.company.admin.service;

import com.highfive.meetu.domain.company.admin.dto.CompanyAdminDTO;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.company.common.repository.CompanyAdminRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyAdminService {

  private final CompanyAdminRepository companyAdminRepository;

  public Page<CompanyAdminDTO> getCompanyList(String status, String keyword, String sort, Pageable pageable) {
    return companyAdminRepository.findAllWithPagingByStatusAndKeywordAndSort(status, keyword, sort, pageable);
  }

  public CompanyAdminDTO getCompanyDetail(Long companyId) {
    Company company = companyAdminRepository.findById(companyId);
    if (company == null) throw new NotFoundException("해당 기업을 찾을 수 없습니다.");

    int postingCount = company.getJobPostingList().size();
    int managerCount = company.getAccountList().size();

    return CompanyAdminDTO.build(company, postingCount, managerCount);
  }

  @Transactional
  public void approveCompany(Long companyId) {
    Company company = companyAdminRepository.findById(companyId);
    if (company == null) throw new NotFoundException("기업 없음");
    company.setStatus(1); // 활성
  }

  @Transactional
  public void rejectCompany(Long companyId) {
    Company company = companyAdminRepository.findById(companyId);
    if (company == null) throw new NotFoundException("기업 없음");
    company.setStatus(2); // 비활성
  }

  @Transactional
  public void blockCompany(Long companyId) {
    Company company = companyAdminRepository.findById(companyId);
    if (company == null) throw new NotFoundException("기업 없음");
    company.setStatus(0); // 대기
  }
}
