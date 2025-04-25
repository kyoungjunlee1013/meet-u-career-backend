package com.highfive.meetu.domain.company.admin.controller;

import com.highfive.meetu.domain.company.admin.dto.CompanyAdminDTO;
import com.highfive.meetu.domain.company.admin.service.CompanyAdminService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/companies")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RequiredArgsConstructor
public class CompanyAdminController {

  private final CompanyAdminService companyAdminService;

  @GetMapping
  public ResultData<Page<CompanyAdminDTO>> getCompanies(
      Pageable pageable,
      @RequestParam(defaultValue = "all") String status,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "default") String sort
  ) {
    Page<CompanyAdminDTO> page = companyAdminService.getCompanyList(status, keyword, sort, pageable);
    return ResultData.success((int) page.getTotalElements(), page);
  }

  @GetMapping("/{companyId}")
  public ResultData<CompanyAdminDTO> getCompanyDetail(@PathVariable Long companyId) {
    CompanyAdminDTO dto = companyAdminService.getCompanyDetail(companyId);
    return ResultData.success(1, dto);
  }

  @PostMapping("/{companyId}/approve")
  public ResultData<?> approve(@PathVariable Long companyId) {
    companyAdminService.approveCompany(companyId);
    return ResultData.success(1, null);
  }

  @PostMapping("/{companyId}/reject")
  public ResultData<?> reject(@PathVariable Long companyId) {
    companyAdminService.rejectCompany(companyId);
    return ResultData.success(1, null);
  }

  @PostMapping("/{companyId}/block")
  public ResultData<?> block(@PathVariable Long companyId) {
    companyAdminService.blockCompany(companyId);
    return ResultData.success(1, null);
  }
}
